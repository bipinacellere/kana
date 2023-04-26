package com.mariten.kanatools;
import com.mariten.kanatools.KanaAppraiser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
  * Easy back-and-forth conversion of kana, hankaku, zenkaku, and other characters used in Japanese text.
  *
  * <p><b>For example code, see the <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/">class details page</a></b></p>
  *
  * <p>Perform multiple conversions on Kana and Roma-ji text with just a single static function call</p>
  * <ul>
  *   <li>Specify one or more conversion operations using flag-style integer constants representing each available type of convesion</li>
  *   <li>Call the <code>convertKana</code> function once on an input string it will quickly handle each conversion in a single run</li>
  *   <li>Designed with speed in mind</li>
  *   <li>Optionally able to specify characters to be excluded from conversion</li>
  *   <li>Uses UTF-8</li>
  *   <li>Inspired by <a target="_blank" href="http://php.net/manual/en/function.mb-convert-kana.php">mb_convert_kana</a>, a native PHP function that similarly handles multi-method Japanese text conversions.</li>
  * </ul>
  *
  * @author Jeff Case (mariten)
  */
public class libConverter
{
     // Conversion Operations Types
    //// Matched numeric values to originals in PHP's source code
    //// https://github.com/php/php-src/blob/a84e5dc37dc0ff8c313164d9db141d3d9f2b2730/ext/mbstring/mbstring.c#L3434

    /**
      * <b>Conversion Op Flag</b>: Standard-width (<i>hankaku</i>) ASCII to double-width (<i>zenkaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_HAN_ASCII_TO_ZEN_ASCII      = 0x00000001;

    /**
      * <b>Conversion Op Flag</b>: Standard-width (<i>hankaku</i>) alphabetic letters to double-width (<i>zenkaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_HAN_LETTER_TO_ZEN_LETTER    = 0x00000002;

    /**
      * <b>Conversion Op Flag</b>: Standard-width (<i>hankaku</i>) numbers to double-width (<i>zenkaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_HAN_NUMBER_TO_ZEN_NUMBER    = 0x00000004;

    /**
      * <b>Conversion Op Flag</b>: Standard-width (<i>hankaku</i>) spaces to double-width (<i>zenkaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_HAN_SPACE_TO_ZEN_SPACE      = 0x00000008;

    /**
      * <b>Conversion Op Flag</b>: Half-width (<i>hankaku</i>) katakana to full-width (<i>zenkaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_HAN_KATA_TO_ZEN_KATA        = 0x00000100;

    /**
      * <b>Conversion Op Flag</b>: Half-width (<i>hankaku</i>) katakana to full-width (<i>zenkaku</i>) hiragana.
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_HAN_KATA_TO_ZEN_HIRA        = 0x00000200;

    /**
      * <b>Conversion Op Flag</b>: Keep <i>hankaku</i> katakana diacritic marks separate.
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_KEEP_DIACRITIC_MARKS_APART  = 0x00100000;

    /**
      * <b>Conversion Op Flag</b>: Double-width (<i>zenkaku</i>) ASCII characters to standard-width (<i>hankaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_ASCII_TO_HAN_ASCII      = 0x00000010;

    /**
      * <b>Conversion Op Flag</b>: Double-width (<i>zenkaku</i>) alphabetic letters to standard-width (<i>hankaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_LETTER_TO_HAN_LETTER    = 0x00000020;

    /**
      * <b>Conversion Op Flag</b>: Double-width (<i>zenkaku</i>) numbers to standard-width (<i>hankaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_NUMBER_TO_HAN_NUMBER    = 0x00000040;

    /**
      * <b>Conversion Op Flag</b>: Double-width (<i>zenkaku</i>) spaces to standard-width (<i>hankaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_SPACE_TO_HAN_SPACE      = 0x00000080;

    /**
      * <b>Conversion Op Flag</b>: Full-width (<i>zenkaku</i>) katakana to half-width (<i>hankaku</i>).
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_KATA_TO_HAN_KATA        = 0x00001000;

    /**
      * <b>Conversion Op Flag</b>: Full-width (<i>zenkaku</i>) hirgana to half-width (<i>hankaku</i>) katakana.
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_HIRA_TO_HAN_KATA        = 0x00002000;

    /**
      * <b>Conversion Op Flag</b>: Full-width (<i>zenkaku</i>) hiragana to full-width (<i>hankaku</i>) katakana.
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_HIRA_TO_ZEN_KATA        = 0x00010000;

    /**
      * <b>Conversion Op Flag</b>: Full-width (<i>zenkaku</i>) katakana to full-width (<i>zenkaku</i>) hiragana.
      * See <a target="_blank" href="http://mariten.github.io/kanatools-java/kana-converter/#conversion-list">Conversion Op Guide</a> for full details.
      */
    public static final int OP_ZEN_KATA_TO_ZEN_HIRA        = 0x00020000;


    public static String convertKana(String original_string, int conversion_ops)
    {
        return convertKana(original_string, conversion_ops, "");
    }
}
