/*
 * Copyright 2015 Open mHealth
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

package com.pzybrick.iote2e.stream.omhext;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.openmhealth.schema.domain.omh.Unit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum HKWorkoutActivityUnit implements Unit {
	// https://developer.apple.com/documentation/healthkit/hkworkoutactivitytype
    CYCLING("HKWorkoutActivityTypeCycling"),
    ELLIPTICAL("HKWorkoutActivityTypeElliptical");

    private String schemaValue;
    private static final Map<String, HKWorkoutActivityUnit> constantsBySchemaValue = new HashMap<>();

    static {
        for (HKWorkoutActivityUnit constant : values()) {
            constantsBySchemaValue.put(constant.getSchemaValue(), constant);
        }
    }

    HKWorkoutActivityUnit(String schemaValue) {
        this.schemaValue = schemaValue;
    }

    @Override
    @JsonValue
    public String getSchemaValue() {
        return this.schemaValue;
    }

    @Nullable
    @JsonCreator
    public static HKWorkoutActivityUnit findBySchemaValue(String schemaValue) {
        return constantsBySchemaValue.get(schemaValue);
    }
}
