package org.yaoqiang.bpmn.intellij.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.bpmn.intellij.editor.BPMNFileEditor;

/**
 * Created by Blenta on 5/30/2017.
 */
public class BPMNPaletteToolWindowManager extends AbstractProjectComponent implements Disposable {

    protected final FileEditorManager myFileEditorManager;

    protected volatile ToolWindow toolWindow;

    private MessageBusConnection messageBusConnection;

    private final BPMNPalette palette;

    protected BPMNPaletteToolWindowManager(Project project, FileEditorManager fileEditorManager) {
        super(project);

        myFileEditorManager = fileEditorManager;
        palette = new BPMNPalette(project);
        Disposer.register(myProject, palette);
    }

    public static BPMNPaletteToolWindowManager getInstance(Project project) {
        return project.getComponent(BPMNPaletteToolWindowManager.class);
    }

    @Override
    @NonNls
    @NotNull
    public String getComponentName() {
        return "BPMNPaletteManager";
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {

    }

    @Override
    public void projectOpened() {
        initListeners();
    }

    @Override
    public void projectClosed() {
        removeListeners();
    }

    private void initListeners() {
        messageBusConnection = myProject.getMessageBus().connect();
        messageBusConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {

            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                updateToolWindow(getActiveBPMNFileEditor());
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                updateToolWindow(getActiveBPMNFileEditor());
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                updateToolWindow(getActiveBPMNFileEditor());
            }
        });

    }

    @Nullable
    public BPMNFileEditor getActiveBPMNFileEditor() {
        for (FileEditor editor : myFileEditorManager.getSelectedEditors()) {
            if (editor instanceof BPMNFileEditor) {
                return (BPMNFileEditor) editor;
            }
        }

        return null;
    }

    private void updateToolWindow(final BPMNFileEditor editor) {
        if (toolWindow == null) {
            if (editor == null) {
                return;
            }
            initToolWindow();
        }

        if (editor == null) {
            toolWindow.setAvailable(false, null);
        } else {
            toolWindow.setAvailable(true, null);
            toolWindow.show(null);
        }
    }

    private void removeListeners() {
        messageBusConnection.disconnect();
        messageBusConnection = null;
    }

    protected void initToolWindow() {
        toolWindow = ToolWindowManager.getInstance(myProject)
                .registerToolWindow("BPMN Palette", false, ToolWindowAnchor.RIGHT, myProject, true);
        toolWindow.setIcon(AllIcons.Toolwindows.ToolWindowPalette);

        ContentManager contentManager = toolWindow.getContentManager();
        Content content = contentManager.getFactory().createContent(palette, null, false);
        content.setCloseable(false);
        content.setPreferredFocusableComponent(palette);
        contentManager.addContent(content);
        contentManager.setSelectedContent(content, true);
        toolWindow.setAvailable(false, null);
    }

    @Override
    public void dispose() {
        toolWindow = null;
    }
}
