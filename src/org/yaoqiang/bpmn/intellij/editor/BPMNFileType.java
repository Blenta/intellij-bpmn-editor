package org.yaoqiang.bpmn.intellij.editor;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Blenta on 5/30/2017.
 */
@Deprecated
public class BPMNFileType implements FileType {

    public static final BPMNFileType INSTANCE = new BPMNFileType();

    @NotNull
    @Override
    public String getName() {
        return "BPMN_EDITOR";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "OMG BPMN 2.0 XML";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "bpmn";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Xml;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
        return CharsetToolkit.UTF8;
    }
}
