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
package guru.mwangaza.eap.xmi.reader

import groovy.xml.Namespace
import guru.mwangaza.uml.ParameterSubstitution
import guru.mwangaza.uml.UmlClass
import guru.mwangaza.uml.UmlModel
import guru.mwangaza.uml.UmlTemplateBinding
import guru.mwangaza.uml.UmlTemplateParameter
import guru.mwangaza.uml.UmlTemplateSignature

/**
 * Created by cnanjo on 11/3/16.
 */
class TemplateBindingReader {
    def uml
    def xmi
    def propertyReader
    def idToParamMap

    public TemplateBindingReader(XmiReaderContext context) {
        this.uml = context.getNamespace("uml")
        this.xmi = context.getNamespace("xmi")
    }

    /**
     *
     * @param node
     * @param model
     * @return
     */
    public UmlTemplateBinding readTemplateBinding(Node node, UmlModel model) {
        def umlTemplateBinding = new UmlTemplateBinding()
        umlTemplateBinding.setId(node.attribute(xmi.id))

        //Note that sometimes signature and actual/formal are parameters and sometimes they are elements

        def templateSignatureIdRef = node.'@signature'
        if(templateSignatureIdRef == null) {
            node.signature.each { it ->
                templateSignatureIdRef = it.'@href'
            }
        }
        umlTemplateBinding.setSignatureRefId(templateSignatureIdRef)

        node.parameterSubstitution.each { sub ->
            def parameterSubstitution = new ParameterSubstitution();
            umlTemplateBinding.addBinding(parameterSubstitution)
            def parameterSubstitutionId = sub.attribute(xmi.id)
            parameterSubstitution.setId(parameterSubstitutionId);

            def actualIdRef = sub.'@actual'
            if(actualIdRef == null) {
                sub.actual.each { actual ->
                    actualIdRef = actual.'@href'
                }
            }
            parameterSubstitution.setActualParameterRefId(actualIdRef)

            def formalIdRef = sub.'@formal'
            if(formalIdRef == null) {
                sub.formal.each { formal ->
                    formalIdRef = formal.'@href'
                }
            }
            parameterSubstitution.setFormalParameterRefId(formalIdRef)
        }
        return umlTemplateBinding
    }

    def processTemplateBinding(Node classNode, UmlClass parent, UmlModel model) {
        def umlTemplateBinding = readTemplateBinding(classNode, model)
        parent.setTemplateBinding(umlTemplateBinding)
    }
}
