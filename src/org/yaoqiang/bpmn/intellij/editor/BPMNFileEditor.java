package org.yaoqiang.bpmn.intellij.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.bpmn.graph.io.BPMNCodec;
import org.yaoqiang.model.util.XMLModelUtils;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Created by Blenta on 5/30/2017.
 */
public class BPMNFileEditor implements FileEditor {

    @NotNull
    private final VirtualFile myFile;

    @NotNull
    private final Document myDocument;

    @NotNull
    private final Project myProject;

    private final BPMNEditorDiagramTab editor;

    public BPMNFileEditor(@NotNull final Project project, @NotNull final VirtualFile file) {
        final VirtualFile vf = file instanceof LightVirtualFile ? ((LightVirtualFile) file).getOriginalFile() : file;
        final Module module = ModuleUtilCore.findModuleForFile(vf, project);
        if (module == null) {
            throw new IllegalArgumentException("No module for file " + file + " in project " + project);
        }
        this.myFile = file;
        this.myDocument = FileDocumentManager.getInstance().getDocument(file);
        this.myProject = project;
        this.editor = new BPMNEditorDiagramTab(this, project, module, file);
    }

    @NotNull
    @Override
    public BPMNEditorDiagramTab getComponent() {
        return editor;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return editor;
    }

    @NotNull
    @Override
    public String getName() {
        return "Diagram";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void selectNotify() {

    }

    @Override
    public void deselectNotify() {

    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override
    public void dispose() {

    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

    }

    public void saveChanges() {
        ApplicationManager.getApplication().invokeLater(() -> {
            String content = XMLModelUtils.getXml(new BPMNCodec(editor.getGraph()).encode().getDocumentElement());
            ApplicationManager.getApplication().runWriteAction(() -> CommandProcessor.getInstance().executeCommand(myProject, () -> myDocument.setText(convertString(content)), "BPMN Diagram edit operation", null));
        });
    }

    public void saveToFile() {
        ApplicationManager.getApplication().invokeLater(() -> {
            ApplicationManager.getApplication().runWriteAction(() -> FileDocumentManager.getInstance().saveDocument(myDocument));
        });
    }

    private String convertString(String content) {
        return content.replaceAll("(\r\n|\n\r|\r)", "\n");
    }

}
