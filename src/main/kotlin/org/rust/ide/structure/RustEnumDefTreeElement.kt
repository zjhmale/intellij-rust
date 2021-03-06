package org.rust.ide.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import org.rust.lang.core.psi.RustEnumDef

class RustEnumDefTreeElement(element: RustEnumDef) : PsiTreeElementBase<RustEnumDef>(element) {

    override fun getPresentableText() = element?.identifier?.text

    override fun getChildrenBase() = arrayListOf<StructureViewTreeElement>()
}
