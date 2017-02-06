package com.khartec.waltz.model.survey;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.khartec.waltz.model.IdProvider;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JsonSerialize(as = ImmutableSurveyQuestionResponseChange.class)
@JsonDeserialize(as = ImmutableSurveyQuestionResponseChange.class)
public abstract class SurveyQuestionResponseChange implements IdProvider {

    public abstract Long questionId();
    public abstract Optional<String> comment();
    public abstract Optional<String> stringResponse();
    public abstract Optional<Double> numberResponse();
    public abstract Optional<Boolean> booleanResponse();
}
