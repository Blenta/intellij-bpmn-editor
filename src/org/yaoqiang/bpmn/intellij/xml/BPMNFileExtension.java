package org.yaoqiang.bpmn.intellij.xml;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.TagNameReference;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.xml.DefaultXmlExtension;
import com.intellij.xml.util.XmlUtil;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.bpmn.intellij.editor.BPMNFileTypeFactory;

/**
 * Created by Blenta on 5/31/2017.
 */
public class BPMNFileExtension extends DefaultXmlExtension {

    public boolean isAvailable(final PsiFile file) {
        return BPMNFileTypeFactory.isBPMN(file);
    }

    public TagNameReference createTagNameReference(final ASTNode nameElement, final boolean startTagFlag) {
        return new BPMNTagNameReference(nameElement, startTagFlag);
    }

    @Nullable
    @Override
    public String[][] getNamespacesFromDocument(XmlDocument parent, boolean declarationsExist) {
        return XmlUtil.getDefaultNamespaces(parent);
    }

}
