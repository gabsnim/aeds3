package ListaInvertida;

import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Collections;

public class LIAux 
{
    // Definir stopwords
    private static String[] stopwords;

    public LIAux()
    {
        stopwords = new String[265];
        init();
    }

    private static void init()
{
    stopwords[0] = "a";
    stopwords[1] = "ainda";
    stopwords[2] = "algo";
    stopwords[3] = "algum";
    stopwords[4] = "algumas";
    stopwords[5] = "alguns";
    stopwords[6] = "alguém";
    stopwords[7] = "ambas";
    stopwords[8] = "ambos";
    stopwords[9] = "ante";
    stopwords[10] = "antes";
    stopwords[11] = "ao";
    stopwords[12] = "aonde";
    stopwords[13] = "aos";
    stopwords[14] = "após";
    stopwords[15] = "aquela";
    stopwords[16] = "aquelas";
    stopwords[17] = "aquele";
    stopwords[18] = "aqueles";
    stopwords[19] = "aquilo";
    stopwords[20] = "as";
    stopwords[21] = "atrás";
    stopwords[22] = "até";
    stopwords[23] = "bem";
    stopwords[24] = "com";
    stopwords[25] = "como";
    stopwords[26] = "consigo";
    stopwords[27] = "contigo";
    stopwords[28] = "contra";
    stopwords[29] = "contudo";
    stopwords[30] = "da";
    stopwords[31] = "das";
    stopwords[32] = "de";
    stopwords[33] = "dela";
    stopwords[34] = "delas";
    stopwords[35] = "dele";
    stopwords[36] = "deles";
    stopwords[37] = "depois";
    stopwords[38] = "desde";
    stopwords[39] = "desse";
    stopwords[40] = "desta";
    stopwords[41] = "deste";
    stopwords[42] = "disto";
    stopwords[43] = "do";
    stopwords[44] = "dos";
    stopwords[45] = "e";
    stopwords[46] = "ela";
    stopwords[47] = "elas";
    stopwords[48] = "ele";
    stopwords[49] = "eles";
    stopwords[50] = "em";
    stopwords[51] = "enquanto";
    stopwords[52] = "entre";
    stopwords[53] = "era";
    stopwords[54] = "eram";
    stopwords[55] = "essa";
    stopwords[56] = "essas";
    stopwords[57] = "esse";
    stopwords[58] = "esses";
    stopwords[59] = "esta";
    stopwords[60] = "estamos";
    stopwords[61] = "estas";
    stopwords[62] = "estava";
    stopwords[63] = "estavam";
    stopwords[64] = "este";
    stopwords[65] = "esteja";
    stopwords[66] = "estejam";
    stopwords[67] = "estejamos";
    stopwords[68] = "estes";
    stopwords[69] = "esteve";
    stopwords[70] = "estive";
    stopwords[71] = "estivemos";
    stopwords[72] = "estiver";
    stopwords[73] = "estivera";
    stopwords[74] = "estiveram";
    stopwords[75] = "estiverem";
    stopwords[76] = "estivermos";
    stopwords[77] = "estivesse";
    stopwords[78] = "estivessem";
    stopwords[79] = "estivéramos";
    stopwords[80] = "estivéssemos";
    stopwords[81] = "estou";
    stopwords[82] = "está";
    stopwords[83] = "estávamos";
    stopwords[84] = "estão";
    stopwords[85] = "eu";
    stopwords[86] = "foi";
    stopwords[87] = "fomos";
    stopwords[88] = "for";
    stopwords[89] = "fora";
    stopwords[90] = "foram";
    stopwords[91] = "forem";
    stopwords[92] = "formos";
    stopwords[93] = "fosse";
    stopwords[94] = "fossem";
    stopwords[95] = "fui";
    stopwords[96] = "fôramos";
    stopwords[97] = "fôssemos";
    stopwords[98] = "haja";
    stopwords[99] = "hajam";
    stopwords[100] = "hajamos";
    stopwords[101] = "havemos";
    stopwords[102] = "havia";
    stopwords[103] = "hei";
    stopwords[104] = "houve";
    stopwords[105] = "houvemos";
    stopwords[106] = "houver";
    stopwords[107] = "houvera";
    stopwords[108] = "houveram";
    stopwords[109] = "houverei";
    stopwords[110] = "houverem";
    stopwords[111] = "houveremos";
    stopwords[112] = "houveria";
    stopwords[113] = "houveriam";
    stopwords[114] = "houvermos";
    stopwords[115] = "houverá";
    stopwords[116] = "houverão";
    stopwords[117] = "houveríamos";
    stopwords[118] = "houvesse";
    stopwords[119] = "houvessem";
    stopwords[120] = "houvéramos";
    stopwords[121] = "houvéssemos";
    stopwords[122] = "há";
    stopwords[123] = "hão";
    stopwords[124] = "isso";
    stopwords[125] = "isto";
    stopwords[126] = "já";
    stopwords[127] = "lhe";
    stopwords[128] = "lhes";
    stopwords[129] = "lá";
    stopwords[130] = "mais";
    stopwords[131] = "mas";
    stopwords[132] = "me";
    stopwords[133] = "mesma";
    stopwords[134] = "mesmo";
    stopwords[135] = "meu";
    stopwords[136] = "meus";
    stopwords[137] = "minha";
    stopwords[138] = "minhas";
    stopwords[139] = "muita";
    stopwords[140] = "muitas";
    stopwords[141] = "muito";
    stopwords[142] = "muitos";
    stopwords[143] = "na";
    stopwords[144] = "nas";
    stopwords[145] = "nem";
    stopwords[146] = "no";
    stopwords[147] = "nos";
    stopwords[148] = "nossa";
    stopwords[149] = "nossas";
    stopwords[150] = "nosso";
    stopwords[151] = "nossos";
    stopwords[152] = "num";
    stopwords[153] = "numa";
    stopwords[154] = "não";
    stopwords[155] = "nós";
    stopwords[156] = "o";
    stopwords[157] = "onde";
    stopwords[158] = "os";
    stopwords[159] = "ou";
    stopwords[160] = "para";
    stopwords[161] = "pela";
    stopwords[162] = "pelas";
    stopwords[163] = "pelo";
    stopwords[164] = "pelos";
    stopwords[165] = "por";
    stopwords[166] = "porque";
    stopwords[167] = "porém";
    stopwords[168] = "possa";
    stopwords[169] = "possam";
    stopwords[170] = "posso";
    stopwords[171] = "pouca";
    stopwords[172] = "poucas";
    stopwords[173] = "pouco";
    stopwords[174] = "poucos";
    stopwords[175] = "primeira";
    stopwords[176] = "primeiro";
    stopwords[177] = "quais";
    stopwords[178] = "qual";
    stopwords[179] = "quando";
    stopwords[180] = "quanta";
    stopwords[181] = "quantas";
    stopwords[182] = "quanto";
    stopwords[183] = "quantos";
    stopwords[184] = "que";
    stopwords[185] = "quem";
    stopwords[186] = "quê";
    stopwords[187] = "se";
    stopwords[188] = "seja";
    stopwords[189] = "sejam";
    stopwords[190] = "sejamos";
    stopwords[191] = "sem";
    stopwords[192] = "ser";
    stopwords[193] = "serei";
    stopwords[194] = "seremos";
    stopwords[195] = "seria";
    stopwords[196] = "seriam";
    stopwords[197] = "será";
    stopwords[198] = "serão";
    stopwords[199] = "seríamos";
    stopwords[200] = "seu";
    stopwords[201] = "seus";
    stopwords[202] = "sob";
    stopwords[203] = "sobre";
    stopwords[204] = "somos";
    stopwords[205] = "sou";
    stopwords[206] = "sua";
    stopwords[207] = "suas";
    stopwords[208] = "são";
    stopwords[209] = "só";
    stopwords[210] = "talvez";
    stopwords[211] = "também";
    stopwords[212] = "te";
    stopwords[213] = "tem";
    stopwords[214] = "temos";
    stopwords[215] = "tenha";
    stopwords[216] = "tenham";
    stopwords[217] = "tenhamos";
    stopwords[218] = "tenho";
    stopwords[219] = "ter";
    stopwords[220] = "terei";
    stopwords[221] = "teremos";
    stopwords[222] = "teria";
    stopwords[223] = "teriam";
    stopwords[224] = "terá";
    stopwords[225] = "terão";
    stopwords[226] = "teríamos";
    stopwords[227] = "teu";
    stopwords[228] = "teus";
    stopwords[229] = "teve";
    stopwords[230] = "tinha";
    stopwords[231] = "tinham";
    stopwords[232] = "tive";
    stopwords[233] = "tivemos";
    stopwords[234] = "tiver";
    stopwords[235] = "tivera";
    stopwords[236] = "tiveram";
    stopwords[237] = "tiverem";
    stopwords[238] = "tivermos";
    stopwords[239] = "tivesse";
    stopwords[240] = "tivessem";
    stopwords[241] = "tivéramos";
    stopwords[242] = "tivéssemos";
    stopwords[243] = "tu";
    stopwords[244] = "tua";
    stopwords[245] = "tuas";
    stopwords[246] = "tudo";
    stopwords[247] = "tém";
    stopwords[248] = "têm";
    stopwords[249] = "tínhamos";
    stopwords[250] = "um";
    stopwords[251] = "uma";
    stopwords[252] = "umas";
    stopwords[253] = "uns";
    stopwords[254] = "vai";
    stopwords[255] = "vem";
    stopwords[256] = "você";
    stopwords[257] = "vocês";
    stopwords[258] = "vos";
    stopwords[259] = "vão";
    stopwords[260] = "vêm";
    stopwords[261] = "à";
    stopwords[262] = "às";
    stopwords[263] = "é";
    stopwords[264] = "éramos";
}

    private static String normalize (String str) 
    {
        if (str != null) {
            String nm = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pt = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pt.matcher(nm).replaceAll("");
        }
        return null;
    }

    public static float[] getFrequency (String[] terms)
    {
        float[] res = null;
        if (terms != null && terms.length > 0)
        {
            terms = Arrays.copyOf(terms, terms.length);
            Arrays.sort(terms, Collections.reverseOrder());

            int n = terms.length;
            int[] counter = new int[n];
            String[] used = new String[n];
            int ui = 0;

            for (int i = 0; i < n; i++) {
                String aux = terms[i];
                int found = -1;
                for (int j = 0; j < n; j++) {
                    if (aux.equals(used[j])) {
                        found = j;
                    }
                }
                if (found > -1) {
                    counter[found]++;
                } else {
                    used[ui] = aux;
                    counter[ui++]++;
                }
            }

            res = new float[ui];
            for (int i = 0; i < ui; i++) {
                res[i] = ((float)counter[i]/(float)n);
            }
        }
        return res;
    }

    public static String[] getTerms (String str)
    {
        String[] res = null;
        String[] palavras = str.split(" ");
        int n = palavras.length;
        int c = 0;

        for (int i = 0; i < n; i++)
        {
            palavras[i] = palavras[i].toLowerCase();
            boolean isStopword = false;
            for (int j = 0; j < stopwords.length; j++)
            {
                if (palavras[i].equals(stopwords[j]))
                {
                    palavras[i] = "";
                    isStopword = true;
                    break;
                }
            }
            if (!isStopword) c++;
        }

        if (c > 0)
        {
            res = new String[c];
            int j = 0;
            for (int i = 0; i < n; i++)
            {
                if (!palavras[i].equals(""))
                {
                    res[j++] = normalize(palavras[i]);
                }
            }
        }
        return res;
    }

    private static int getMax (ElementoLista[][] EL)
    {
        int max = 0;
        for (ElementoLista[] elArr : EL)
        {
            for (ElementoLista el : elArr)
            {
                if (el.getId() > max)
                    max = el.getId();
            }
        }
        return max;
    }

    public static int[] getQueryOrder (ElementoLista[][] EL, int IDCount) throws Exception
    {
        if (EL == null) return null;
        int n = EL.length;
        ElementoLista[][] OEL = EL.clone();
        int aTPN = getMax(EL)+1;

        float[] aTP = new float[aTPN];
        int[] idTP = new int[aTPN];
        Arrays.fill(aTP, -1);

        int c = 0;
        for (int i = 0; i < n; i++)
        {
            int TupN = EL[i].length;
            float IDF = (float)(Math.log10((double)IDCount/(double)TupN)) + 1;

            for (int j = 0; j < TupN; j++)
            {
                ElementoLista tuple = EL[i][j];
                float fq = OEL[i][j].getFrequencia();
                int id = tuple.getId();

                if (aTP[id] != -1)
                    aTP[id] += fq * IDF;
                else {
                    aTP[id] = fq * IDF;
                    idTP[c++] = id;
                }
            }
        }

        for (int i = 1; i < c; i++)
        {
            int tmp = idTP[i];
            int j = i - 1;
            while (j >= 0 && aTP[idTP[j]] < aTP[tmp])
            {
                idTP[j + 1] = idTP[j];
                j--;
            }
            idTP[j + 1] = tmp;
        }

        return Arrays.copyOf(idTP, c);
    }
}