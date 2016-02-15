package com.thoughtworks.go.strongauth.authentication.principalDetailSources;

import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetail;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.io.IOUtils.toBufferedReader;

public class ConfigurableUserPrincipalDetailSource implements PrincipalDetailSource {
    private final Map<String, PrincipalDetail> principalDetails = new HashMap<>();
    public static final Pattern DETAIL_PATTERN = Pattern.compile("^([^:]+):([^:]+):([^:]+):([^:]+)$");

    @SneakyThrows(IOException.class)
    public ConfigurableUserPrincipalDetailSource(InputStream passwordFile) {
        String line;
        while ((line = toBufferedReader(new InputStreamReader(passwordFile)).readLine()) != null) {
            Optional<PrincipalDetail> maybeDetail = parse(line);
            if (maybeDetail.isPresent()) {
                PrincipalDetail detail = maybeDetail.get();
                principalDetails.put(detail.getUsername(), detail);
            }
        }
    }



    private Optional<PrincipalDetail> parse(final String line) {
        final Matcher matcher = DETAIL_PATTERN.matcher(line);
        if (matcher.find()) {
            return Optional.of(new PrincipalDetail(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4))
            );
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<PrincipalDetail> byUsername(String username) {
        return Optional.fromNullable(principalDetails.get(username));
    }
}