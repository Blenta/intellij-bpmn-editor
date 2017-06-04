package org.yaoqiang.bpmn.intellij.xml;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.xml.XmlSchemaProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.bpmn.intellij.editor.BPMNFileTypeFactory;
import org.yaoqiang.model.bpmn.BPMNModelConstants;

import java.net.URL;

/**
 * Created by Blenta on 5/31/2017.
 */
public class BPMNFileSchemaProvider extends XmlSchemaProvider {

    @Override
    public boolean isAvailable(final @NotNull XmlFile file) {
        return BPMNFileTypeFactory.isBPMN(file);
    }

    @Nullable
    @Override
    public XmlFile getSchema(@NotNull @NonNls String url, @Nullable Module module, @NotNull PsiFile baseFile) {
        return module != null && BPMNFileTypeFactory.isBPMN(baseFile) ? getReference(url, module) : null;
    }

    private static XmlFile getReference(@NotNull @NonNls String url, @NotNull Module module) {
        if (url.equalsIgnoreCase("http://bpmn.sourceforge.net")) {
            return null;
        }
        final URL resource = BPMNFileSchemaProvider.class.getResource(BPMNModelConstants.BPMN_SCHEMA_BASEURI + BPMNModelConstants.BPMN_SYSTEM_ID);
        final VirtualFile fileByURL = VfsUtil.findFileByURL(resource);
        if (fileByURL == null) {
            return null;
        }

        PsiFile psiFile = PsiManager.getInstance(module.getProject()).findFile(fileByURL);
        return (XmlFile) psiFile.copy();
    }

}
