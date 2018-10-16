package org.thoughtcrime.securesms.crypto;


import android.content.Context;
import android.support.annotation.NonNull;

import org.thoughtcrime.securesms.util.Base64;
import org.thoughtcrime.securesms.util.TextSecurePreferences;
import org.thoughtcrime.securesms.util.Util;

import java.io.IOException;

public class ProfileKeyUtil {

  public static synchronized boolean hasProfileKey(@NonNull Context context) {
    return TextSecurePreferences.getProfileKey(context) != null;
  }

  public static synchronized @NonNull byte[] getProfileKey(@NonNull Context context) {
    try {
      String encodedProfileKey = TextSecurePreferences.getProfileKey(context);

      if (encodedProfileKey == null) {
        encodedProfileKey = generateAndStoreProfileKey(context);
      }

      return Base64.decode(encodedProfileKey);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  public static synchronized @NonNull byte[] rotateProfileKey(@NonNull Context context) {
    generateAndStoreProfileKey(context);
    return getProfileKey(context);
  }

  private static synchronized String generateAndStoreProfileKey(@NonNull Context context) {
    String encodedKey = Util.getSecret(32);
    TextSecurePreferences.setProfileKey(context, encodedKey);
    return encodedKey;
  }
}
