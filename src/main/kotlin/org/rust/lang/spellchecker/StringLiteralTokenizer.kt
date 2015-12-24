package org.rust.lang.spellchecker

import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.tokenizer.EscapeSequenceTokenizer
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer

class StringLiteralTokenizer : Tokenizer<LeafPsiElement>() {

    override fun tokenize(element: LeafPsiElement, consumer: TokenConsumer) {
        element.text.let {
            when ("\\" !in it) {
                true -> consumer.consumeToken(element, PlainTextSplitter.getInstance())
                else -> processTextWithEscapeSequences(element, it, consumer)
            }
        }
    }

    companion object {
        fun processTextWithEscapeSequences(element: LeafPsiElement, text: String, consumer: TokenConsumer) {
            val unescapedText = StringBuilder()
            val offsets = IntArray(text.length + 1)

            PsiLiteralExpressionImpl.parseStringCharacters(text, unescapedText, offsets)

            EscapeSequenceTokenizer.processTextWithOffsets(element, consumer, unescapedText, offsets, 1)
        }
    }
}
