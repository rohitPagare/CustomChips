package com.orhotech.multichiptextview.validator;

import androidx.annotation.NonNull;

import com.orhotech.multichiptextview.tokenizer.ChipTokenizer;

/**
 * Interface used to ensure that a given CharSequence complies to a particular format.
 */
public interface MultiChipValidator {
    /**
     * Validates the specified text.
     *
     * @return true If the text currently in the text editor is valid.
     * @see #fixText(ChipTokenizer, CharSequence)
     */
    boolean isValid(@NonNull ChipTokenizer chipTokenizer, CharSequence text);

    /**
     * Corrects the specified text to make it valid.
     *
     * @param invalidText A string that doesn't pass validation: isValid(invalidText)
     *                    returns false
     * @return A string based on invalidText such as invoking isValid() on it returns true.
     * @see #isValid(ChipTokenizer, CharSequence)
     */
    CharSequence fixText(@NonNull ChipTokenizer chipTokenizer, CharSequence invalidText);

}
