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

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;

import org.openmhealth.schema.domain.omh.Measure;
import org.openmhealth.schema.domain.omh.SchemaId;
import org.openmhealth.schema.domain.omh.TypedUnitValue;
import org.openmhealth.schema.serializer.SerializationConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(LowerCaseWithUnderscoresStrategy.class)
public class HKWorkoutActivity extends Measure {

    public static final SchemaId SCHEMA_ID = new SchemaId(OMH_NAMESPACE, "hk-workout", "1.0");

    private TypedUnitValue<HKWorkoutActivityUnit> workoutActivity;


    @SerializationConstructor
    protected HKWorkoutActivity() {
    }

    public static class Builder extends Measure.Builder<HKWorkoutActivity, Builder> {

        private TypedUnitValue<HKWorkoutActivityUnit> workoutActivity;
        public Builder(TypedUnitValue<HKWorkoutActivityUnit> workoutActivity) {

            checkNotNull(workoutActivity, "An activity hasn't been specified.");
            this.workoutActivity = workoutActivity;
        }

        /**
         * @param workoutActivityValue the heart rate in beats per minute
         */
        public Builder(BigDecimal workoutActivityValue) {

            checkNotNull(workoutActivityValue, "A heart rate hasn't been specified.");
            this.workoutActivity = new TypedUnitValue<>(HKWorkoutActivityUnit.CYCLING, workoutActivityValue);
        }

        /**
         * @param workoutActivityValue the heart rate in beats per minute
         */
        public Builder(double workoutActivityValue) {
            this(BigDecimal.valueOf(workoutActivityValue));
        }

        /**
         * @param workoutActivityValue the heart rate in beats per minute
         */
        public Builder(long workoutActivityValue) {
            this(BigDecimal.valueOf(workoutActivityValue));
        }

//        public Builder setTemporalRelationshipToPhysicalActivity(TemporalRelationshipToPhysicalActivity relationship) {
//            this.temporalRelationshipToPhysicalActivity = relationship;
//            return this;
//        }

        @Override
        public HKWorkoutActivity build() {
            return new HKWorkoutActivity(this);
        }
    }

    private HKWorkoutActivity(Builder builder) {
        super(builder);

        this.workoutActivity = builder.workoutActivity;
//        this.temporalRelationshipToPhysicalActivity = builder.temporalRelationshipToPhysicalActivity;
    }

    public String getActivityName() {
        return workoutActivity.getUnit();
    }

//    public TemporalRelationshipToPhysicalActivity getTemporalRelationshipToPhysicalActivity() {
//        return temporalRelationshipToPhysicalActivity;
//    }

    @Override
    public SchemaId getSchemaId() {
        return SCHEMA_ID;
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HKWorkoutActivity other = (HKWorkoutActivity) obj;
		if (workoutActivity == null) {
			if (other.workoutActivity != null)
				return false;
		} else if (!workoutActivity.equals(other.workoutActivity))
			return false;
		return true;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((workoutActivity == null) ? 0 : workoutActivity.hashCode());
		return result;
	}
}
