// Copyright 2023 Goldman Sachs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.finos.legend.connection;

import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.authentication.specification.AuthenticationSpecification;
import org.finos.legend.engine.shared.core.identity.Credential;
import org.finos.legend.engine.shared.core.identity.Identity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public abstract class CredentialBuilder<SPEC extends AuthenticationSpecification, INPUT_CRED extends Credential, OUTPUT_CRED extends Credential>
{
    public abstract OUTPUT_CRED makeCredential(Identity identity, SPEC spec, INPUT_CRED cred, EnvironmentConfiguration configuration) throws Exception;

    public Class<? extends AuthenticationSpecification> getAuthenticationSpecificationType()
    {
        return (Class<? extends AuthenticationSpecification>) actualTypeArguments()[0];
    }

    public Class<? extends Credential> getInputCredentialType()
    {
        return (Class<? extends Credential>) actualTypeArguments()[1];
    }

    public Class<? extends Credential> getOutputCredentialType()
    {
        return (Class<? extends Credential>) actualTypeArguments()[2];
    }

    private Type[] actualTypeArguments()
    {
        Type genericSuperClass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;
        return parameterizedType.getActualTypeArguments();
    }

    public static class Key
    {
        private final Class<? extends AuthenticationSpecification> authenticationSpecificationType;
        private final Class<? extends Credential> inputCredentialType;
        private final Class<? extends Credential> outputCredentialType;

        public Key(Class<? extends AuthenticationSpecification> authenticationSpecificationType, Class<? extends Credential> inputCredentialType, Class<? extends Credential> outputCredentialType)
        {
            this.authenticationSpecificationType = authenticationSpecificationType;
            this.inputCredentialType = inputCredentialType;
            this.outputCredentialType = outputCredentialType;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            CredentialBuilder.Key that = (CredentialBuilder.Key) o;
            return this.authenticationSpecificationType.equals(that.authenticationSpecificationType) &&
                    this.inputCredentialType.equals(that.inputCredentialType) &&
                    this.outputCredentialType.equals(that.outputCredentialType);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(authenticationSpecificationType, inputCredentialType, outputCredentialType);
        }
    }
}