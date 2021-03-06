package org.molgenis.data.support;

import java.io.IOException;
import javax.annotation.Nullable;
import org.molgenis.data.meta.model.Attribute;

class TemplateExpressionException extends RuntimeException {
  TemplateExpressionException(Attribute attribute) {
    this(attribute, null);
  }

  TemplateExpressionException(Attribute attribute, @Nullable IOException e) {
    super("Attribute " + attribute.getName() + " expression is null", e);
  }
}
