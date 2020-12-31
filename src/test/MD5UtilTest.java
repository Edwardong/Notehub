import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * @author Deren Lei
 * @version CS48
 * @see MD5Util
 */


public class MD5UtilTest {

    @Test
    public void testHash1() {

        String hash = MD5Util.md5Hex("derenlei@umail.ucsb.edu");

        assertEquals("11b3db7607912c80e47864750fe0c51b", hash);
    }

    @Test
    public void testHash2() {

        String hash = MD5Util.md5Hex("zichen_sun@umail.ucsb.edu");

        assertEquals("cf42177ec1c802bee4dade58f9f14951", hash);

    }

    @Test
    public void testHash3() {

        String hash = MD5Util.md5Hex("delinSun@umail.ucsb.edu");

        assertEquals("71ebb803189b86f2aca62d2e775652c0", hash);

    }


}
