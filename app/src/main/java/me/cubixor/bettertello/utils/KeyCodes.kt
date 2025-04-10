package me.cubixor.bettertello.utils

object KeyCodes {

    val keyCodes = arrayOf(
        /*0*/ "UNKNOWN",
        /*1*/ "SOFT_LEFT",
        /*2*/ "SOFT_RIGHT",
        /*3*/ "HOME",
        /*4*/ "BACK",
        /*5*/ "CALL",
        /*6*/ "ENDCALL",
        /*7*/ "0",
        /*8*/ "1",
        /*9*/ "2",
        /*10*/ "3",
        /*11*/ "4",
        /*12*/ "5",
        /*13*/ "6",
        /*14*/ "7",
        /*15*/ "8",
        /*16*/ "9",
        /*17*/ "STAR",
        /*18*/ "POUND",
        /*19*/ "DPAD_UP",
        /*20*/ "DPAD_DOWN",
        /*21*/ "DPAD_LEFT",
        /*22*/ "DPAD_RIGHT",
        /*23*/ "DPAD_CENTER",
        /*24*/ "VOLUME_UP",
        /*25*/ "VOLUME_DOWN",
        /*26*/ "POWER",
        /*27*/ "CAMERA",
        /*28*/ "CLEAR",
        /*29*/ "A",
        /*30*/ "B",
        /*31*/ "C",
        /*32*/ "D",
        /*33*/ "E",
        /*34*/ "F",
        /*35*/ "G",
        /*36*/ "H",
        /*37*/ "I",
        /*38*/ "J",
        /*39*/ "K",
        /*40*/ "L",
        /*41*/ "M",
        /*42*/ "N",
        /*43*/ "O",
        /*44*/ "P",
        /*45*/ "Q",
        /*46*/ "R",
        /*47*/ "S",
        /*48*/ "T",
        /*49*/ "U",
        /*50*/ "V",
        /*51*/ "W",
        /*52*/ "X",
        /*53*/ "Y",
        /*54*/ "Z",
        /*55*/ "COMMA",
        /*56*/ "PERIOD",
        /*57*/ "ALT_LEFT",
        /*58*/ "ALT_RIGHT",
        /*59*/ "SHIFT_LEFT",
        /*60*/ "SHIFT_RIGHT",
        /*61*/ "TAB",
        /*62*/ "SPACE",
        /*63*/ "SYM",
        /*64*/ "EXPLORER",
        /*65*/ "ENVELOPE",
        /*66*/ "ENTER",
        /*67*/ "DEL",
        /*68*/ "GRAVE",
        /*69*/ "MINUS",
        /*70*/ "EQUALS",
        /*71*/ "LEFT_BRACKET",
        /*72*/ "RIGHT_BRACKET",
        /*73*/ "BACKSLASH",
        /*74*/ "SEMICOLON",
        /*75*/ "APOSTROPHE",
        /*76*/ "SLASH",
        /*77*/ "AT",
        /*78*/ "NUM",
        /*79*/ "HEADSETHOOK",
        /*80*/ "FOCUS",
        /*81*/ "PLUS",
        /*82*/ "MENU",
        /*83*/ "NOTIFICATION",
        /*84*/ "SEARCH",
        /*85*/ "MEDIA_PLAY_PAUSE",
        /*86*/ "MEDIA_STOP",
        /*87*/ "MEDIA_NEXT",
        /*88*/ "MEDIA_PREVIOUS",
        /*89*/ "MEDIA_REWIND",
        /*90*/ "MEDIA_FAST_FORWARD",
        /*91*/ "MUTE",
        /*92*/ "PAGE_UP",
        /*93*/ "PAGE_DOWN",
        /*94*/ "PICTSYMBOLS",
        /*95*/ "SWITCH_CHARSET",
        /*96*/ "BUTTON_A",
        /*97*/ "BUTTON_B",
        /*98*/ "BUTTON_C",
        /*99*/ "BUTTON_X",
        /*100*/ "BUTTON_Y",
        /*101*/ "BUTTON_Z",
        /*102*/ "BUTTON_L1",
        /*103*/ "BUTTON_R1",
        /*104*/ "BUTTON_L2",
        /*105*/ "BUTTON_R2",
        /*106*/ "BUTTON_THUMBL",
        /*107*/ "BUTTON_THUMBR",
        /*108*/ "BUTTON_START",
        /*109*/ "BUTTON_SELECT",
        /*110*/ "BUTTON_MODE",
        /*111*/ "ESCAPE",
        /*112*/ "FORWARD_DEL",
        /*113*/ "CTRL_LEFT",
        /*114*/ "CTRL_RIGHT",
        /*115*/ "CAPS_LOCK",
        /*116*/ "SCROLL_LOCK",
        /*117*/ "META_LEFT",
        /*118*/ "META_RIGHT",
        /*119*/ "FUNCTION",
        /*120*/ "SYSRQ",
        /*121*/ "BREAK",
        /*122*/ "MOVE_HOME",
        /*123*/ "MOVE_END",
        /*124*/ "INSERT",
        /*125*/ "FORWARD",
        /*126*/ "MEDIA_PLAY",
        /*127*/ "MEDIA_PAUSE",
        /*128*/ "MEDIA_CLOSE",
        /*129*/ "MEDIA_EJECT",
        /*130*/ "MEDIA_RECORD",
        /*131*/ "F1",
        /*132*/ "F2",
        /*133*/ "F3",
        /*134*/ "F4",
        /*135*/ "F5",
        /*136*/ "F6",
        /*137*/ "F7",
        /*138*/ "F8",
        /*139*/ "F9",
        /*140*/ "F10",
        /*141*/ "F11",
        /*142*/ "F12",
        /*143*/ "NUM_LOCK",
        /*144*/ "NUMPAD_0",
        /*145*/ "NUMPAD_1",
        /*146*/ "NUMPAD_2",
        /*147*/ "NUMPAD_3",
        /*148*/ "NUMPAD_4",
        /*149*/ "NUMPAD_5",
        /*150*/ "NUMPAD_6",
        /*151*/ "NUMPAD_7",
        /*152*/ "NUMPAD_8",
        /*153*/ "NUMPAD_9",
        /*154*/ "NUMPAD_DIVIDE",
        /*155*/ "NUMPAD_MULTIPLY",
        /*156*/ "NUMPAD_SUBTRACT",
        /*157*/ "NUMPAD_ADD",
        /*158*/ "NUMPAD_DOT",
        /*159*/ "NUMPAD_COMMA",
        /*160*/ "NUMPAD_ENTER",
        /*161*/ "NUMPAD_EQUALS",
        /*162*/ "NUMPAD_LEFT_PAREN",
        /*163*/ "NUMPAD_RIGHT_PAREN",
        /*164*/ "VOLUME_MUTE",
        /*165*/ "INFO",
        /*166*/ "CHANNEL_UP",
        /*167*/ "CHANNEL_DOWN",
        /*168*/ "ZOOM_IN",
        /*169*/ "ZOOM_OUT",
        /*170*/ "TV",
        /*171*/ "WINDOW",
        /*172*/ "GUIDE",
        /*173*/ "DVR",
        /*174*/ "BOOKMARK",
        /*175*/ "CAPTIONS",
        /*176*/ "SETTINGS",
        /*177*/ "TV_POWER",
        /*178*/ "TV_INPUT",
        /*179*/ "STB_POWER",
        /*180*/ "STB_INPUT",
        /*181*/ "AVR_POWER",
        /*182*/ "AVR_INPUT",
        /*183*/ "PROG_RED",
        /*184*/ "PROG_GREEN",
        /*185*/ "PROG_YELLOW",
        /*186*/ "PROG_BLUE",
        /*187*/ "APP_SWITCH",
        /*188*/ "BUTTON_1",
        /*189*/ "BUTTON_2",
        /*190*/ "BUTTON_3",
        /*191*/ "BUTTON_4",
        /*192*/ "BUTTON_5",
        /*193*/ "BUTTON_6",
        /*194*/ "BUTTON_7",
        /*195*/ "BUTTON_8",
        /*196*/ "BUTTON_9",
        /*197*/ "BUTTON_10",
        /*198*/ "BUTTON_11",
        /*199*/ "BUTTON_12",
        /*200*/ "BUTTON_13",
        /*201*/ "BUTTON_14",
        /*202*/ "BUTTON_15",
        /*203*/ "BUTTON_16",
        /*204*/ "LANGUAGE_SWITCH",
        /*205*/ "MANNER_MODE",
        /*206*/ "3D _MODE",
        /*207*/ "CONTACTS",
        /*208*/ "CALENDAR",
        /*209*/ "MUSIC",
        /*210*/ "CALCULATOR",
        /*211*/ "ZENKAKU_HANKAKU",
        /*212*/ "EISU",
        /*213*/ "MUHENKAN",
        /*214*/ "HENKAN",
        /*215*/ "KATAKANA_HIRAGANA",
        /*216*/ "YEN",
        /*217*/ "RO",
        /*218*/ "KANA",
        /*219*/ "ASSIST",
        /*220*/ "BRIGHTNESS_DOWN",
        /*221*/ "BRIGHTNESS_UP",
        /*222*/ "MEDIA_AUDIO_TRACK",
        /*223*/ "SLEEP",
        /*224*/ "WAKEUP",
        /*225*/ "PAIRING",
        /*226*/ "MEDIA_TOP_MENU",
        /*227*/ "11",
        /*228*/ "12",
        /*229*/ "LAST_CHANNEL",
        /*230*/ "TV_DATA_SERVICE",
        /*231*/ "VOICE_ASSIST",
        /*232*/ "TV_RADIO_SERVICE",
        /*233*/ "TV_TELETEXT",
        /*234*/ "TV_NUMBER_ENTRY",
        /*235*/ "TV_TERRESTRIAL_ANALOG",
        /*236*/ "TV_TERRESTRIAL_DIGITAL",
        /*237*/ "TV_SATELLITE",
        /*238*/ "TV_SATELLITE_BS",
        /*239*/ "TV_SATELLITE_CS",
        /*240*/ "TV_SATELLITE_SERVICE",
        /*241*/ "TV_NETWORK",
        /*242*/ "TV_ANTENNA_CABLE",
        /*243*/ "TV_INPUT_HDMI_1",
        /*244*/ "TV_INPUT_HDMI_2",
        /*245*/ "TV_INPUT_HDMI_3",
        /*246*/ "TV_INPUT_HDMI_4",
        /*247*/ "TV_INPUT_COMPOSITE_1",
        /*248*/ "TV_INPUT_COMPOSITE_2",
        /*249*/ "TV_INPUT_COMPONENT_1",
        /*250*/ "TV_INPUT_COMPONENT_2",
        /*251*/ "TV_INPUT_VGA_1",
        /*252*/ "TV_AUDIO_DESCRIPTION",
        /*253*/ "TV_AUDIO_DESCRIPTION_MIX_UP",
        /*254*/ "TV_AUDIO_DESCRIPTION_MIX_DOWN",
        /*255*/ "TV_ZOOM_MODE",
        /*256*/ "TV_CONTENTS_MENU",
        /*257*/ "TV_MEDIA_CONTEXT_MENU",
        /*258*/ "TV_TIMER_PROGRAMMING",
        /*259*/ "HELP",
        /*260*/ "NAVIGATE_PREVIOUS",
        /*261*/ "NAVIGATE_NEXT",
        /*262*/ "NAVIGATE_IN",
        /*263*/ "NAVIGATE_OUT",
        /*264*/ "STEM_PRIMARY",
        /*265*/ "STEM_1",
        /*266*/ "STEM_2",
        /*267*/ "STEM_3",
        /*268*/ "DPAD_UP_LEFT",
        /*269*/ "DPAD_DOWN_LEFT",
        /*270*/ "DPAD_UP_RIGHT",
        /*271*/ "DPAD_DOWN_RIGHT",
        /*272*/ "MEDIA_SKIP_FORWARD",
        /*273*/ "MEDIA_SKIP_BACKWARD",
        /*274*/ "MEDIA_STEP_FORWARD",
        /*275*/ "MEDIA_STEP_BACKWARD",
        /*276*/ "SOFT_SLEEP",
        /*277*/ "CUT",
        /*278*/ "COPY",
        /*279*/ "PASTE",
        /*280*/ "SYSTEM_NAVIGATION_UP",
        /*281*/ "SYSTEM_NAVIGATION_DOWN",
        /*282*/ "SYSTEM_NAVIGATION_LEFT",
        /*283*/ "SYSTEM_NAVIGATION_RIGHT",
        /*284*/ "ALL_APPS",
        /*285*/ "REFRESH",
        /*286*/ "THUMBS_UP",
        /*287*/ "THUMBS_DOWN",
        /*288*/ "PROFILE_SWITCH"
    )
}