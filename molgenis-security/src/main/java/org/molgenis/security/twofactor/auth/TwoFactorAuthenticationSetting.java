package org.molgenis.security.twofactor.auth;

/**
 * You can have tw-factor-authentication in three different states in your system
 *
 * <ul>
 *   <li>DISABLED: two-factor-authentication is disabled for all users
 *   <li>ENABLED: two-factor-authentication is enabled. Users CAN determine for themselves if they
 *       want to configure it in MOLGENIS
 *   <li>ENFORCES: two-factor-authentication is enforced for all users. Users HAVE TO configure
 *       two-factor-authentication for their account.
 * </ul>
 */
public enum TwoFactorAuthenticationSetting {
  ENABLED("Enabled"),
  DISABLED("Disabled"),
  ENFORCED("Enforced");

  private final String label;

  TwoFactorAuthenticationSetting(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static TwoFactorAuthenticationSetting fromLabel(String label) {
    return valueOf(label.toUpperCase());
  }
}
