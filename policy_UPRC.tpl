accept(Certificate) :-

  extract(Certificate, format, eIDAS_qualified_certificate),

  extract(Certificate, trustScheme, TrustSchemeClaim),

  trustschemeX(TrustSchemeClaim),
  trustlist(TrustSchemeClaim, Certificate, TrustListEntry),
  extract(TrustListEntry, format, trustlist_entry),

  extract(TrustListEntry, pubKey, PkIss).

trustschemeX(TrustSchemeClaim) :-
  trustscheme(TrustSchemeClaim, peppol1_qualified).

trustschemeX(TrustSchemeClaim) :-
  trustscheme(TrustSchemeClaim, peppol2_qualified).

