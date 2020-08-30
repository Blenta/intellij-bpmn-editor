/**
 * Yaoqiang BPMN Editor
 * Copyright (C) 2009-2017 Shi Yaoqiang(shi_yaoqiang@yahoo.com), All rights reserved.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.yaoqiang.bpmn.intellij;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.asaf.*;
import org.yaoqiang.bpmn.editor.actions.BPMNEditorActions;
import org.yaoqiang.bpmn.editor.menus.ModelMenu;
import org.yaoqiang.bpmn.editor.utils.BPMNEditorUtils;
import org.yaoqiang.bpmn.intellij.actions.NewBPMNFileAction;
import org.yaoqiang.bpmn.intellij.editor.BPMNFileEditor;
import org.yaoqiang.graph.actions.GraphActions;
import org.yaoqiang.graph.util.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by Blenta on 6/3/2017.
 */
public class BPMNEditor implements ApplicationComponent {

    private MessageBusConnection messageBusConnection;

    @Override
    public void initComponent() {

        System.setProperty("asaf.app.name", "plugin");
        ASAF.setVersion("2.0.0 (Intellij Plugin)");

        String currentLanguage = ASAF.getSetting(ASAF.KEY_LANGUAGE, ASAF.DEFAULT_LANGUAGE);
        ASAF.setLocale(new Locale(currentLanguage));
        ResourceMap.add("org.yaoqiang.asaf.resources.locale");
        ResourceMap.add("org.yaoqiang.graph.resources.graph");
        ResourceMap.add("org.yaoqiang.bpmn.editor.resources.locale");
        ASAF.setLogo(ResourceMap.getImageIcon("yaoqiang.Menu.icon").getImage());

        initialize();

        StaticActionMap.addActions(new GraphActions());
        StaticActionMap.addActions(new BPMNEditorActions());
        initBPMNModelMenu();

        initListeners();
    }

    private void initialize() {
        File udir = new File(Constants.YAOQIANG_USER_HOME);
        if (!udir.exists()) {
            try {
                udir.mkdir();
            } catch (Exception exc) {
            }
        }

        File adir = new File(Constants.YAOQIANG_ARTIFACTS_DIR);
        if (!adir.exists()) {
            try {
                adir.mkdir();
            } catch (Exception exc) {
            }
        }

        File fdir = new File(Constants.YAOQIANG_FRAGMENTS_DIR);
        if (!fdir.exists()) {
            try {
                fdir.mkdir();
            } catch (Exception exc) {
            }
        }

        Constants.SETTINGS = loadYaoqiangConfigureFile();

        mxConstants.DEFAULT_STARTSIZE = Integer.parseInt(Constants.SETTINGS.getProperty("style_swimlane_title_size", "25"));

        Constants.LAST_FILLCOLOR.clear();
        String lastFileColor = Constants.SETTINGS.getProperty("lastFillColor", "");
        List<String> colorList = Arrays.asList(lastFileColor.split(","));
        int cn = 12;
        for (String color : colorList) {
            if (color.length() < 1) {
                continue;
            }
            cn--;
            Constants.LAST_FILLCOLOR.add(mxUtils.parseColor(color));
        }
        int i = 0;
        while (cn > 0) {
            cn--;
            Constants.LAST_FILLCOLOR.add(Constants.COLORS[i++]);
        }

        BPMNEditorUtils.setDefaultPageFormat();
    }

    private void initListeners() {
        messageBusConnection = ApplicationManager.getApplication().getMessageBus().connect();

        messageBusConnection.subscribe(FileEditorManagerListener.Before.FILE_EDITOR_MANAGER, new FileEditorManagerListener.Before() {
            @Override
            public void beforeFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
            }

            @Override
            public void beforeFileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                BPMNFileEditor editor = getBPMNFileEditor(source, file);
                if (editor != null) {
                    editor.saveChanges();
                    editor.saveToFile();
                }
            }
        });

        messageBusConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {

            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                if (event.getNewFile() == event.getOldFile()) {// switch inner editor tabs
                    Boolean newCreated = event.getNewFile().getUserData(NewBPMNFileAction.NEW_CREATED);
                    if (newCreated == null || !newCreated) {
                        if (event.getNewEditor() instanceof TextEditor) {
                            BPMNFileEditor editor = getBPMNFileEditor(event.getManager(), event.getNewFile());
                            if (editor != null) {
                                editor.saveChanges();
                            }
                        }
                    } else {
                        if (newCreated && !(event.getNewEditor() instanceof BPMNFileEditor)) {// switch back to Diagram tab for new file
                            event.getNewFile().putUserData(NewBPMNFileAction.NEW_CREATED, false);
                            ApplicationManager.getApplication().invokeLater(() -> setBPMNFileEditor(event.getManager(), event.getNewFile()));// wait for template variables set completed
                        }
                    }
                } else if (event.getNewFile() != null) {
                    if (event.getNewEditor() instanceof BPMNFileEditor) {// switched to bpmn file
                        ASAF.setSelectedFileTab(((BPMNFileEditor) event.getNewEditor()).getComponent().getFileTab());
                    } else {// switched to other type file
                        ASAF.setSelectedFileTab(null);
                    }
                }

            }
        });
    }

    private void setBPMNFileEditor(@NotNull FileEditorManager manager, @NotNull VirtualFile file) {
        manager.setSelectedEditor(file, "bpmn-editor");
    }

    @Nullable
    private BPMNFileEditor getBPMNFileEditor(@NotNull FileEditorManager manager, @NotNull VirtualFile file) {
        for (FileEditor editor : manager.getAllEditors(file)) {
            if (editor instanceof BPMNFileEditor) {
                return (BPMNFileEditor) editor;
            }
        }

        return null;
    }

    public void initBPMNModelMenu() {
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup modelMenu = (DefaultActionGroup) actionManager.getAction("yaoqiang.actions.BPMNModelMenu");
        AppMenu model = new ModelMenu();
        for (int i = 0; i < model.getItemCount(); i++) {
            JMenuItem item = model.getItem(i);
            if (item == null) {
                modelMenu.addSeparator();
            } else {
                AnAction action = new AnAction(item.getText()) {
                    @Override
                    public void actionPerformed(AnActionEvent e) {
                        item.getAction().actionPerformed(new ActionEvent(e, 0, ""));
                    }
                };
                modelMenu.add(action);
            }
        }
    }

    @Override
    public void disposeComponent() {
        messageBusConnection.disconnect();
        messageBusConnection = null;
        Utils.saveASAFProperties(ASAF.getSettings());
        saveYaoqiangConfigureFile();
    }

    private Properties loadYaoqiangConfigureFile() {
        File file = new File(Constants.YAOQIANG_CONF_FILE);

        Properties props = new Properties();
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                props.load(fis);
                fis.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return props;
    }

    private void saveYaoqiangConfigureFile() {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(Constants.YAOQIANG_CONF_FILE));
            Constants.SETTINGS.store(out, "Yaoqiang BPMN Editor Configuration");
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "BPMN Editor";
    }
}
