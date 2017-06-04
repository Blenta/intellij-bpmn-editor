package org.yaoqiang.bpmn.intellij.editor;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Blenta on 5/30/2017.
 */
public class BPMNFileTypeFactory extends FileTypeFactory {

    @NonNls
    public static final String BPMN_EXTENSION = "bpmn";
    @NonNls
    static final String DOT_BPMN_EXTENSION = "." + BPMN_EXTENSION;

    public static boolean isBPMN(@NotNull PsiFile file) {
        final VirtualFile virtualFile = file.getViewProvider().getVirtualFile();
        return isBPMN(virtualFile);
    }

    public static boolean isBPMN(@NotNull VirtualFile virtualFile) {
        if (BPMN_EXTENSION.equals(virtualFile.getExtension())) {
            final FileType fileType = virtualFile.getFileType();
            if (fileType == getFileType() && !fileType.isBinary()) {
                return virtualFile.getName().endsWith(DOT_BPMN_EXTENSION);
            }
        }
        return false;
    }

    @NotNull
    public static FileType getFileType() {
        return FileTypeManager.getInstance().getFileTypeByExtension(BPMN_EXTENSION);
    }

    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        final FileType fileType = consumer.getStandardFileTypeByName("XML");
        assert fileType != null;
        consumer.consume(fileType, BPMN_EXTENSION);
    }

}
