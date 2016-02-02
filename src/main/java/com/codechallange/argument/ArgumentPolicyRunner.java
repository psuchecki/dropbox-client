package com.codechallange.argument;

import com.dropbox.core.DbxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ArgumentPolicyRunner {
    @Autowired
    private List<ArgumentPolicy> argumentPolicies;

    public void runArgumentPolicies(String[] args) throws IOException, DbxException {
        ArgumentPolicy argumentPolicy = argumentPolicies.stream()
                .filter(policy -> policy.usePolicy(args))
                .findFirst()
                .get();

        argumentPolicy.processArguments(args);
    }
}
