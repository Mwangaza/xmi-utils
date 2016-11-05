package guru.mwangaza.eap.xmi.reader

import groovy.xml.Namespace
import guru.mwangaza.uml.UmlClass
import guru.mwangaza.uml.UmlModel
import guru.mwangaza.uml.UmlTemplateParameter
import guru.mwangaza.uml.UmlTemplateSignature

/**
 * Created by cnanjo on 10/3/16.
 */
class TemplateSignatureReader {

    def uml
    def xmi
    def propertyReader
    def idToParamMap

    public TemplateSignatureReader(Namespace uml, Namespace xmi) {
        this.uml = uml
        this.xmi = xmi
        propertyReader = new PropertyReader(uml, xmi)
        idToParamMap = new HashMap<String, UmlTemplateParameter>()
    }

    public UmlTemplateSignature readTemplate(Node node, UmlModel model) {
        def umlTemplateSignature = new UmlTemplateSignature()
        umlTemplateSignature.setId(node.attribute(xmi.id))

        def templateSignatureId = node.attribute(xmi.id);
        model.putObject(templateSignatureId, umlTemplateSignature)

        //Handle reference to parameters
        node.parameter.each { it ->
            UmlTemplateParameter param = new UmlTemplateParameter()
            param.setId(it.attribute(xmi.idref))
            param.setParameterReference(true)
            umlTemplateSignature.addTemplateParameter(param)
            idToParamMap.put(param.getId(), param)
        }

        node.ownedParameter.each { it ->
            UmlTemplateParameter param = null;
            it.ownedParameteredElement.each { ope ->
                def id = ope.'@templateParameter'
                param = idToParamMap.get(id)
                if(param == null) { //Do we have a parameter reference? If not, create new param, else, use the one created for the reference and fill its detailed here
                    param = new UmlTemplateParameter()
                }
                param.setParameterReference(false);
                param.setName(ope.'@name')
                param.setId(ope.'@templateParameter')
                param.setElementId(ope.attribute(xmi.id))
                model.putObject(param.getId(), param)
                model.putObject(param.getElementId(), umlTemplateSignature)
            }
            it.constrainingClassifier.each { cc ->
                param.setTypeId(cc.attribute(xmi.idref))
            }
        }
        return umlTemplateSignature
    }

    def processTemplateSignature(Node classNode, UmlClass parent, UmlModel model) {
        def umlTemplateSignature = readTemplate(classNode, model)
        parent.setTemplateSignature(umlTemplateSignature)
    }
}