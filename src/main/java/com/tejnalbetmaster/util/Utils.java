package com.tejnalbetmaster.util;

import com.tejnalbetmaster.data.entity.models.EPrizeMode;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static boolean isCurrentRoundFree(EPrizeMode latestPrizeCode) {
		boolean isCurrentRoundFree = false;
		if (null != latestPrizeCode
				&& (latestPrizeCode.equals(EPrizeMode.BIG_AND_FR) || latestPrizeCode.equals(EPrizeMode.SMALL_AND_FR)
						|| latestPrizeCode.equals(EPrizeMode.MEDIUM_AND_FR) || latestPrizeCode.equals(EPrizeMode.FR))) {
			isCurrentRoundFree = true;
		}
		return isCurrentRoundFree;
	}

	public static String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}
