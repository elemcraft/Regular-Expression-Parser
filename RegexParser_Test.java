import java.io.ByteArrayInputStream;
import org.junit.*;

public class RegexParser_Test {
    private static void setUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    public void basic() {
        // no operators
        // special symbols like semicolon, space, and numbers are included
        setUserInput("ko;2 q46");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output = parser.getNFA().match("ko;2 q46");
        boolean output1 = parser.getNFA().match("ko;2q46");
        boolean output2 = parser.getNFA().match("ko2 q46");
        boolean output3 = parser.getNFA().match("ko2q46");
        Assert.assertEquals(output, true);
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
    }

    @Test
    public void emptyRegEx() {
        RegexParser parser = new RegexParser();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("");
        boolean output2 = parser.getNFA().match("a");
        boolean output3 = parser.getNFA().match("*");
        boolean output4 = parser.getNFA().match("|");
        boolean output5 = parser.getNFA().match("+");
        Assert.assertEquals(output1, true);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, false);
    }

    @Test
    public void singleKleeneStar() {
        setUserInput("jsh*fq");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("jsh*fq");
        boolean output2 = parser.getNFA().match("jsfq");
        boolean output3 = parser.getNFA().match("jshfq");
        boolean output4 = parser.getNFA().match("jshhhhhfq");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void singleKleenePlus() {
        setUserInput("jsh+fq");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("jsfq");
        boolean output2 = parser.getNFA().match("jshfq");
        boolean output3 = parser.getNFA().match("jshhhhhfq");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
    }

    @Test
    public void singleAlternation() {
        setUserInput("kofw|ew");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("kofw");
        boolean output2 = parser.getNFA().match("ew");
        boolean output3 = parser.getNFA().match("kofwew");
        boolean output4 = parser.getNFA().match("kofw|ew");
        Assert.assertEquals(output1, true);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
    }

    @Test
    public void onePairOfBrackets() {
        setUserInput("(sbv)oi15");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("(sbv)oi15");
        boolean output2 = parser.getNFA().match("sbvoi15");
        boolean output3 = parser.getNFA().match("");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, false);
    }

    @Test
    public void multipleKleeneStar() {
        setUserInput("62*ewqr*fs");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("62ewqr*fs");
        boolean output2 = parser.getNFA().match("62*ewqrfs");
        boolean output3 = parser.getNFA().match("6ewqfs");
        boolean output4 = parser.getNFA().match("62222ewqrrrfs");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void multipleKleenePlus() {
        setUserInput("gq;+mi29+wnr");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("gq;mi29+wnr");
        boolean output2 = parser.getNFA().match("gq;+mi29wnr");
        boolean output3 = parser.getNFA().match("gqmi29wnr");
        boolean output4 = parser.getNFA().match("gq;mi2wnr");
        boolean output5 = parser.getNFA().match("gq;;;mi2999999wnr");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
    }

    @Test
    public void multipleAlternation() {
        setUserInput("vqr|89r|bj,");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("vqr|89r|bj,");
        boolean output2 = parser.getNFA().match("vqr");
        boolean output3 = parser.getNFA().match("89r");
        boolean output4 = parser.getNFA().match("bj,");
        boolean output5 = parser.getNFA().match("vqr89rbj,");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, false);
    }

    @Test
    public void multipleBrackets() {
        setUserInput("(,rukw35)gtr(ehrw;[])");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("(,rukw35)gtr(ehrw;[])");
        boolean output2 = parser.getNFA().match("(,rukw35)gtrehrw;[]");
        boolean output3 = parser.getNFA().match(",rukw35gtr(ehrw;[])");
        boolean output4 = parser.getNFA().match(",rukw35gtrehrw;[]");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void bracketsAndKleeneStar() {
        setUserInput("tq4(jpl5'7)*vr3");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("tq4(jpl5'7)*vr3");
        boolean output2 = parser.getNFA().match("tq4jpl5'7*vr3");
        boolean output3 = parser.getNFA().match("tq4vr3");
        boolean output4 = parser.getNFA().match("tq4jpl5'7vr3");
        boolean output5 = parser.getNFA().match("tq4jpl5'7jpl5'7jpl5'7vr3");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, true);
    }

    @Test
    public void bracketsAndKleenePlus() {
        setUserInput("oq(ij2;_)+24");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("oq(ij2;_)+24");
        boolean output2 = parser.getNFA().match("oqij2;_+24");
        boolean output3 = parser.getNFA().match("oq(ij2;_)24");
        boolean output4 = parser.getNFA().match("oq24");
        boolean output5 = parser.getNFA().match("oqij2;_24");
        boolean output6 = parser.getNFA().match("oqij2;_ij2;_ij2;_24");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
    }

    @Test
    public void bracketsAndAlternation() {
        setUserInput("t(gt4w|ok)two");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("t(gt4w|ok)two");
        boolean output2 = parser.getNFA().match("tgt4w|oktwo");
        boolean output3 = parser.getNFA().match("t(gt4wok)two");
        boolean output4 = parser.getNFA().match("tgt4woktwo");
        boolean output5 = parser.getNFA().match("tgt4wtwo");
        boolean output6 = parser.getNFA().match("toktwo");
        boolean output7 = parser.getNFA().match("ttwo");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
        Assert.assertEquals(output7, false);
    }

    @Test
    public void multipleDifferentOperators() {
        setUserInput("qh+3|gw*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("qh+3|gw*");
        boolean output2 = parser.getNFA().match("qh+3gw*");
        boolean output3 = parser.getNFA().match("qh3|gw*");
        boolean output4 = parser.getNFA().match("qh+3|gw");
        boolean output5 = parser.getNFA().match("qh3gw");
        boolean output6 = parser.getNFA().match("qh+3");
        boolean output7 = parser.getNFA().match("gw*");
        boolean output8 = parser.getNFA().match("q3");
        boolean output9 = parser.getNFA().match("g");
        boolean output10 = parser.getNFA().match("qh3");
        boolean output11 = parser.getNFA().match("gw");
        boolean output12 = parser.getNFA().match("qhhhhhh3");
        boolean output13 = parser.getNFA().match("gwwwwww");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, false);
        Assert.assertEquals(output6, false);
        Assert.assertEquals(output7, false);
        Assert.assertEquals(output8, false);
        Assert.assertEquals(output9, true);
        Assert.assertEquals(output10, true);
        Assert.assertEquals(output11, true);
        Assert.assertEquals(output12, true);
        Assert.assertEquals(output13, true);
    }

    @Test
    public void bracketsAndOperators() {
        setUserInput("(h*|p)(h|d)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output1 = parser.getNFA().match("(h*|p)(h|d)");
        boolean output2 = parser.getNFA().match("");
        boolean output3 = parser.getNFA().match("d");
        boolean output4 = parser.getNFA().match("h");
        boolean output5 = parser.getNFA().match("hhd");
        boolean output6 = parser.getNFA().match("ph");
        boolean output7 = parser.getNFA().match("pd");
        boolean output8 = parser.getNFA().match("hph");
        boolean output9 = parser.getNFA().match("phd");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
        Assert.assertEquals(output7, true);
        Assert.assertEquals(output8, false);
        Assert.assertEquals(output9, false);
    }

    @Test
    public void bracketsAndOperators2() {
        setUserInput("(t*|f)*r*(k|y)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();
        boolean output = parser.getNFA().match("(t*|f)+r(k|y)");
        boolean output1 = parser.getNFA().match("");
        boolean output2 = parser.getNFA().match("ttt");
        boolean output3 = parser.getNFA().match("fffff");
        boolean output4 = parser.getNFA().match("tff");
        boolean output5 = parser.getNFA().match("rrr");
        boolean output6 = parser.getNFA().match("kkkk");
        boolean output7 = parser.getNFA().match("yy");
        boolean output8 = parser.getNFA().match("ty");
        boolean output9 = parser.getNFA().match("try");
        boolean output10 = parser.getNFA().match("rrry");
        boolean output11 = parser.getNFA().match("fry");
        boolean output12 = parser.getNFA().match("rk");
        boolean output13 = parser.getNFA().match("trk");
        boolean output14 = parser.getNFA().match("tk");
        boolean output15 = parser.getNFA().match("ky");
        Assert.assertEquals(output, false);
        Assert.assertEquals(output1, true);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
        Assert.assertEquals(output7, true);
        Assert.assertEquals(output8, true);
        Assert.assertEquals(output9, true);
        Assert.assertEquals(output10, true);
        Assert.assertEquals(output11, true);
        Assert.assertEquals(output12, true);
        Assert.assertEquals(output13, true);
        Assert.assertEquals(output14, true);
        Assert.assertEquals(output15, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startWithKleenePlus() {
        setUserInput("+a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void startWithAlternation() {
        setUserInput("|a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void startWithKleeneStar() {
        setUserInput("*a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void kleeneStarPrecededByLeftBracket() {
        setUserInput("(*a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void kleenePlusPrecededByLeftBracket() {
        setUserInput("(+a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationPrecededByLeftBracket() {
        setUserInput("(|a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators() {
        setUserInput("a**");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators2() {
        setUserInput("a++");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators3() {
        setUserInput("a||b");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators4() {
        setUserInput("a+*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators5() {
        setUserInput("a*+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unclosedLeftBracket() {
        setUserInput("(a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unclosedRightBracket() {
        setUserInput("a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unclosedBrackets() {
        setUserInput(")a(");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void endWithAlternation() {
        setUserInput("a|");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationFollowedByRightBracket() {
        setUserInput("(a|)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationFollowedByKleeneStar() {
        setUserInput("a|*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationFollowedByKleenePlus() {
        setUserInput("a|+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test
    public void verbose_Basic() {
        setUserInput("0123456789");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        for (int i = 0; i < 9; i++) {
            parser.getNFA().matchSymbol((char) (i + '0'));
            Assert.assertEquals(parser.getNFA().accepting(), false);
        }
        parser.getNFA().matchSymbol('9');
        Assert.assertEquals(parser.getNFA().accepting(), true);
    }

    @Test
    public void verbose_Basic2() {
        setUserInput("abcdefghijklmnopqrstuvwxyz");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        for (int i = 0; i < 25; i++) {
            parser.getNFA().matchSymbol((char) (i + 'a'));
            Assert.assertEquals(parser.getNFA().accepting(), false);
        }
        parser.getNFA().matchSymbol('z');
        Assert.assertEquals(parser.getNFA().accepting(), true);
    }

    @Test
    public void verbose_SingleKleeneStar() {
        setUserInput("k*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), true);

        for (int i = 0; i < 100; i++) {
            parser.getNFA().matchSymbol('k');
            Assert.assertEquals(parser.getNFA().accepting(), true);
        }
    }

    @Test
    public void verbose_SingleKleeneStar2() {
        setUserInput("ab*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('a');
        Assert.assertEquals(parser.getNFA().accepting(), true);

        for (int i = 0; i < 100; i++) {
            parser.getNFA().matchSymbol('b');
            Assert.assertEquals(parser.getNFA().accepting(), true);
        }
    }

    @Test
    public void verbose_SingleKleenePlus() {
        setUserInput("k+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        for (int i = 0; i < 100; i++) {
            parser.getNFA().matchSymbol('k');
            Assert.assertEquals(parser.getNFA().accepting(), true);
        }
    }

    @Test
    public void verbose_SingleKleenePlus2() {
        setUserInput("fh+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('f');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        for (int i = 0; i < 100; i++) {
            parser.getNFA().matchSymbol('h');
            Assert.assertEquals(parser.getNFA().accepting(), true);
        }
    }

    @Test
    public void verbose_SingleAlternation() {
        setUserInput("ab|dc");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('a');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('b');
        Assert.assertEquals(parser.getNFA().accepting(), true);
    }

    @Test
    public void verbose_SingleAlternation2() {
        setUserInput("ab|dc");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('d');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('c');
        Assert.assertEquals(parser.getNFA().accepting(), true);
    }

    @Test
    public void verbose_OnePairOfBrackets() {
        setUserInput("(ab)c");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('a');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('b');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('c');
        Assert.assertEquals(parser.getNFA().accepting(), true);

        parser.getNFA().matchSymbol('a');
        Assert.assertEquals(parser.getNFA().accepting(), false);
    }

    @Test
    public void verbose_MultipleBracketsAndOperators() {
        setUserInput("s(d+|c*)8(wr)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('s');
        Assert.assertEquals(parser.getNFA().accepting(), false);
        
        parser.getNFA().matchSymbol('8');
        Assert.assertEquals(parser.getNFA().accepting(), true);

        parser.getNFA().matchSymbol('w');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('r');
        Assert.assertEquals(parser.getNFA().accepting(), true);

        parser.getNFA().matchSymbol('w');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('r');
        Assert.assertEquals(parser.getNFA().accepting(), true);
    }

    @Test
    public void verbose_MultipleBracketsAndOperators2() {
        setUserInput("s(d+|c*)8(wr)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('s');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('d');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('d');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('d');
        Assert.assertEquals(parser.getNFA().accepting(), false);
        
        parser.getNFA().matchSymbol('8');
        Assert.assertEquals(parser.getNFA().accepting(), true);
    }

    @Test
    public void verbose_MultipleBracketsAndOperators3() {
        setUserInput("s(d+|c*)8(wr)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('s');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('c');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('c');
        Assert.assertEquals(parser.getNFA().accepting(), false);
        
        parser.getNFA().matchSymbol('8');
        Assert.assertEquals(parser.getNFA().accepting(), true);

        parser.getNFA().matchSymbol('w');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('r');
        Assert.assertEquals(parser.getNFA().accepting(), true);

        parser.getNFA().matchSymbol('w');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('r');
        Assert.assertEquals(parser.getNFA().accepting(), true);
    }

    @Test
    public void verbose_emptyRegEx() {
        RegexParser parser = new RegexParser();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().accepting(), true);

        parser.getNFA().matchSymbol(' ');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol(' ');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('a');
        Assert.assertEquals(parser.getNFA().accepting(), false);

        parser.getNFA().matchSymbol('b');
        Assert.assertEquals(parser.getNFA().accepting(), false);
    }
}