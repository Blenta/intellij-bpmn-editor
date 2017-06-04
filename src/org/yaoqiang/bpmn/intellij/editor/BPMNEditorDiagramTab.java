package org.yaoqiang.bpmn.intellij.editor;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.asaf.ASAF;
import org.yaoqiang.asaf.ResourceMap;
import org.yaoqiang.bpmn.editor.actions.AboutYaoqiangAction;
import org.yaoqiang.bpmn.editor.actions.SaveAsPNGAction;
import org.yaoqiang.bpmn.editor.actions.SaveAsSVGAction;
import org.yaoqiang.bpmn.editor.views.BPMNFileTab;
import org.yaoqiang.bpmn.graph.view.BPMNGraph;
import org.yaoqiang.graph.swing.StyleToolBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

/**
 * Created by Blenta on 5/30/2017.
 */
public class BPMNEditorDiagramTab extends JPanel implements DataProvider {

    @NotNull
    private final BPMNFileTab fileTab;

    @NotNull
    private final BPMNFileEditor myEditor;

    @NotNull
    private final Project myProject;

    @NotNull
    private Module myModule;

    @NotNull
    private final VirtualFile myFile;

    private BPMNGraph graph;

    public BPMNEditorDiagramTab(@NotNull BPMNFileEditor editor, Project project, Module module, @NotNull VirtualFile file) {
        setLayout(new BorderLayout());

        this.myProject = project;
        this.myEditor = editor;
        this.myModule = module;
        this.myFile = file;

        this.fileTab = new BPMNFileTab(new File(file.getPath()), false, false);
        installResizeHandler();

        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.setBorder(null);

        toolbarPanel.add(createSaveToolBar());
        toolbarPanel.add(new StyleToolBar(fileTab));
        toolbarPanel.add(createEditToolBar());

        add(toolbarPanel, BorderLayout.NORTH);
        add(fileTab.getViewToolBar(), BorderLayout.EAST);
        add(fileTab.getAlignToolBar(), BorderLayout.WEST);

        add(fileTab.getDiagramPane());
    }

    private JToolBar createSaveToolBar() {
        JToolBar saveBar = new JToolBar();
        saveBar.setFloatable(false);

        saveBar.add(new SaveAsPNGAction() {
            public boolean isEnabled() {
                return true;
            }

            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                myFile.getParent().refresh(true, false);
            }
        });
        saveBar.add(new SaveAsSVGAction() {
            public boolean isEnabled() {
                return true;
            }

            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                myFile.getParent().refresh(true, false);
            }
        });

        return saveBar;
    }

    private JToolBar createEditToolBar() {
        JToolBar editBar = new JToolBar();
        editBar.setFloatable(false);

        editBar.add(ASAF.getAction("undo"));
        editBar.add(ASAF.getAction("redo"));
        editBar.addSeparator();
        editBar.add(ASAF.getAction("cut"));
        editBar.add(ASAF.getAction("copy"));
        editBar.add(ASAF.getAction("paste"));
        editBar.add(ASAF.getAction("delete"));
        editBar.addSeparator();
        Action about = new AboutYaoqiangAction();
        about.putValue(Action.SMALL_ICON, ResourceMap.getImageIcon("yaoqiang.Menu.icon"));
        editBar.add(about);

        return editBar;
    }

    private void installResizeHandler() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                fileTab.getGraphComponent().zoomAndCenter();
            }
        });
    }

    public BPMNGraph getGraph() {
        return fileTab.getGraph();
    }

    @NotNull
    public BPMNFileTab getFileTab() {
        return fileTab;
    }

    @Nullable
    @Override
    public Object getData(String dataId) {
        return null;
    }

}
