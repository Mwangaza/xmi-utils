/**
 * Copyright (C) 2013 - 2017 Claude Nanjo
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by cnanjo.
 */
package guru.mwangaza.uml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a UML class attribute/property.
 * Typically, a property has a name, a type, and a cardinality.
 *
 * @author cnanjo
 */
public class UmlProperty extends UmlComponent implements Cloneable {

    private CardinalityRange cardinality;
    private List<BaseClassifier> types;
    private String typeId;
    private UmlComponent source;
    private List<UmlProperty> mappings;
    private Map<String, String> aliases;
    private Map<String, CardinalityRange> cardinalityConstraints;
    private boolean isGenericType = false;//Indicates whether type is generic
    private UmlVisibilityEnum visibility;

    public UmlProperty() {
        cardinality = new CardinalityRange();
        mappings = new ArrayList<UmlProperty>();
        aliases = new HashMap<String, String>();
        cardinalityConstraints = new HashMap<String, CardinalityRange>();
        types = new ArrayList<BaseClassifier>();
    }

    public UmlProperty(String name) {
        this();
        setName(name);
    }

    public UmlProperty(String name, Integer low, Integer high) {
        this(name);
        this.cardinality.setLow(low);
        this.cardinality.setHigh(high);
    }

    public Integer getLow() {
        return cardinality.getLow();
    }

    public void setLow(Integer low) {
        this.cardinality.setLow(low);
    }

    public Integer getHigh() {
        return cardinality.getHigh();
    }

    public void setHigh(Integer high) {
        this.cardinality.setHigh(high);
    }

    private void setCardinality(CardinalityRange range) {
        this.cardinality = range;
    }

    public List<BaseClassifier> getTypes() {
        return types;
    }

    public void setTypes(List<BaseClassifier> types) {
        this.types = types;
    }

    public BaseClassifier getFirstType() {
        BaseClassifier type = null;
        if (types != null && types.size() > 0) {
            type = types.get(0);
        }
        return type;
    }

    public void addType(BaseClassifier type) {
        types.add(type);
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public UmlComponent getSource() {
        return source;
    }

    public void setSource(UmlComponent source) {
        this.source = source;
    }

    public List<UmlProperty> getMappings() {
        return mappings;
    }

    public void setMappings(List<UmlProperty> mappings) {
        this.mappings = mappings;
    }

    public void addMapping(UmlProperty mapping) {
        this.mappings.add(mapping);
    }

    public Map<String, String> getAliases() {
        return aliases;
    }

    public void setAliases(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    public void addAliasForScope(String scope, String alias) {
        aliases.put(scope, alias);
    }

    public void removeAliasForScope(String scope) {
        aliases.remove(scope);
    }

    public String getAliasForScope(String scope) {
        return aliases.get(scope);
    }

    public void clearAliases() {
        aliases.clear();
    }

    public UmlVisibilityEnum getVisibility() {
        return visibility;
    }

    public void setVisibility(UmlVisibilityEnum visibility) {
        this.visibility = visibility;
    }

    public String getNameForScope(String scope) {
        String alias = getAliasForScope(scope);
        if (alias != null) {
            return alias;
        } else {
            return getName();
        }
    }

    public boolean isGenericType() {
        return isGenericType;
    }

    public void setGenericType(boolean genericType) {
        isGenericType = genericType;
    }

    public Map<String, CardinalityRange> getCardinalityConstraints() {
        return cardinalityConstraints;
    }

    public void setCardinalityConstraints(HashMap<String, CardinalityRange> cardinalityConstraints) {
        this.cardinalityConstraints = cardinalityConstraints;
    }

    public void addCardinalityConstraint(String scope, CardinalityRange cardinalityConstraint) {
        validate(cardinalityConstraint);
        cardinalityConstraints.put(scope, cardinalityConstraint);
    }

    public void removeCardinalityConstraintForScope(String scope) {
        cardinalityConstraints.remove(scope);
    }

    public void clearCardinalityConstraints() {
        cardinalityConstraints.clear();
    }

    public Integer getAdjustedLow(String scope) {
        CardinalityRange constraint = cardinalityConstraints.get(scope);
        Integer adjustedLow = getLow();
        if (constraint != null) {
            if (constraint.getLow() != null) {
                adjustedLow = constraint.getLow();
            }
        }
        return adjustedLow;
    }

    public Integer getAdjustedHigh(String scope) {
        CardinalityRange constraint = cardinalityConstraints.get(scope);
        Integer adjustedHigh = getHigh();
        if (constraint != null) {
            if (constraint.getHigh() != null) {
                adjustedHigh = constraint.getHigh();
            }
        }
        return adjustedHigh;
    }

    public void validate(CardinalityRange cardinalityConstraint) {
        this.cardinality.validate(cardinalityConstraint);
    }

    public UmlProperty clone() {
        UmlProperty clone = (UmlProperty) super.clone();
        clone.setName(this.getName());
        clone.setId(this.getId());
        clone.setLow(this.getLow());
        clone.setHigh(this.getHigh());
        clone.setDocumentation(this.getDocumentation());
        clone.setTypes(new ArrayList<BaseClassifier>());
        clone.getTypes().addAll(this.getTypes());
        clone.setSource(this.getSource());
        clone.setMappings(new ArrayList<UmlProperty>());
        clone.getMappings().addAll(this.getMappings());
        clone.setAliases(new HashMap<String, String>());
        clone.getAliases().putAll(this.getAliases());
        clone.setCardinality(this.cardinality.clone());
        clone.setCardinalityConstraints(new HashMap<String, CardinalityRange>());
        clone.getCardinalityConstraints().putAll(this.getCardinalityConstraints());
        return clone;
    }

    public void findTypeClassForTypeId(UmlModel model) {

        if (typeId == null) {
            System.out.println("Property " + getName() + " has null type id.");
            return;
        }

        Object object = model.getIdMap().get(getTypeId());

        BaseClassifier typeClass = null;
        if (object instanceof UmlClass) {
            typeClass = (UmlClass) model.getIdMap().get(getTypeId());
        } else if (object instanceof UmlInterface) {
            typeClass = (UmlInterface)model.getIdMap().get(getTypeId());
        } else if (object instanceof UmlTemplateSignature) {
            System.out.println("!!!!!!!");
            System.out.println(object);
            System.out.println("=======");
            setGenericType(true);
            UmlTemplateSignature signature = (UmlTemplateSignature) object;
            UmlTemplateParameter param = signature.getParameter(getTypeId());
            if (param != null) {
                typeClass = new UmlClass(param.getName());
                System.out.println("FOUND THE TYPE FOR " + getName() + ". It is " + param.getName() + " and type ID is " + getTypeId());
            } else {
                System.out.println("NO PARAMETER FOUND FOR " + getName() + " with type ID " + getTypeId());
            }

        }
        if (typeClass != null) {
            addType(typeClass);
        } else {
            typeClass = (UmlClass) model.getObjectById(typeId);
            if (typeClass != null) {
                addType(typeClass);
            } else {
                if (getTypeId() == null) {
                    System.out.println("Unknown class ID: " + getTypeId());//TODO Handle subsetting and redefinition
                } else {
                    System.out.println("Unknown class ID: " + getTypeId());//TODO Handle parameters in generics
                }
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (getSource() != null) {
            builder.append(getSource().getName());
        } else {
            builder.append("UNKNOWN_SOURCE");
        }
        builder.append(".").append(getName());
        if (cardinality != null && (cardinality.getLow() != null || cardinality.getHigh() != null)) {
            builder.append("[").append(cardinality.getLow()).append("..").append(cardinality.getHigh()).append("]");
        }
        return builder.toString();
    }
}
