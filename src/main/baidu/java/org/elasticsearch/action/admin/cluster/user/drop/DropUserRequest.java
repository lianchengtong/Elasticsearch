/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticsearch.action.admin.cluster.user.drop;

import java.io.IOException;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.cluster.metadata.UserProperty;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

public class DropUserRequest extends AcknowledgedRequest<DropUserRequest> {

    private String username;
    
    public DropUserRequest() {}
    
    public DropUserRequest(String username) {
        this.username = username;
    }
    
    @Override
    public ActionRequestValidationException validate() {
        String usernameWithoutTenant = UserProperty.getUsernameWithoutTenantFromFullUsername(username);
        if (UserProperty.ROOT_NAME.equalsIgnoreCase(usernameWithoutTenant)) {
            ActionRequestValidationException invalidRequestError = new ActionRequestValidationException();
            invalidRequestError.addValidationError("could drop root user");
            return invalidRequestError;
        }
        return null;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        username = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
