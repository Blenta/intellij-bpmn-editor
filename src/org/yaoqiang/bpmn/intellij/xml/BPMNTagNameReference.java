package org.yaoqiang.bpmn.intellij.xml;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.xml.TagNameReference;

/**
 * Created by Blenta on 5/31/2017.
 */
public class BPMNTagNameReference extends TagNameReference {

    public BPMNTagNameReference(ASTNode nameElement, boolean startTagFlag) {
        super(nameElement, startTagFlag);
    }
}
