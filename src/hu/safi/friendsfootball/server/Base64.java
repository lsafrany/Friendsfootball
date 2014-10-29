package hu.safi.friendsfootball.server;

public class Base64 {

	private static final String etab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

	public static String encode(String data) {
		StringBuffer out = new StringBuffer();

		int i = 0;
		int r = data.length();
		while (r > 0) {
			byte d0, d1, d2;
			byte e0, e1, e2, e3;

			d0 = (byte) data.charAt(i++);
			--r;
			e0 = (byte) (d0 >>> 2);
			e1 = (byte) ((d0 & 0x03) << 4);

			if (r > 0) {
				d1 = (byte) data.charAt(i++);
				--r;
				e1 += (byte) (d1 >>> 4);
				e2 = (byte) ((d1 & 0x0f) << 2);
			} else {
				e2 = 64;
			}

			if (r > 0) {
				d2 = (byte) data.charAt(i++);
				--r;
				e2 += (byte) (d2 >>> 6);
				e3 = (byte) (d2 & 0x3f);
			} else {
				e3 = 64;
			}
			out.append(etab.charAt(e0));
			out.append(etab.charAt(e1));
			out.append(etab.charAt(e2));
			out.append(etab.charAt(e3));
		}

		return out.toString();
	}
}
