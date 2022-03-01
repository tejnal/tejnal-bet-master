package com.tejnalbetmaster.converter;

import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.security.exception.CustomInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, EBetMode> {

  private static final Logger logger = LoggerFactory.getLogger(StringToEnumConverter.class);

  @Override
  public EBetMode convert(String source) {
    try {
      return EBetMode.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      logger.error("Invalid parameter passed as mode.");
      if (null != source)
        throw new CustomInputException(
            "REQUEST PARAM VALIDATION FAILED!", "Invalid parameter value passed: '" + source + "'");
      else return EBetMode.CASH;
    }
  }
}
