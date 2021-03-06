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

import guru.mwangaza.uml.UmlClass
import guru.mwangaza.uml.UmlModel
import guru.mwangaza.uml.UmlPackage
import guru.mwangaza.uml.UmlTemplateBinding
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class TemplateBindingReaderTest {
	
	private UmlModel model
	private List<UmlClass> classes1
	private List<UmlClass> classes2
	
	@Before
	public void setup() {
		//Load first model
		XmiReader xmiReader = XmiReader.configureDefaultXmiReader();
		UmlModel dependency = xmiReader.loadModelFromClassPath("/xmi/GenericTestCore.xml")
		dependency.buildIndex()

		xmiReader = XmiReader.configureDefaultXmiReader();
		Map<String, UmlModel> dependencies = new HashMap<>();
		dependencies.put("GenericTestCore.xml", dependency)
		xmiReader.setDependencies(dependencies)
		model = xmiReader.loadModelFromClassPath("/xmi/GenericTestProject.xml")
		model.buildIndex()
	}

	@Test
	public void testTemplateBinding() {
		List<Package> packages = model.getPackages();
		UmlPackage rootPkg = packages.get(0);
		assertEquals(1, packages.size());
		assertEquals(8, rootPkg.getClasses().size())
		UmlClass dateInterval = model.getObjectByName("DateInterval")
		assertNotNull(dateInterval)
		UmlTemplateBinding binding = dateInterval.getTemplateBinding()
		assertNotNull(binding)
		testBindingDateInterval(binding)

		UmlClass textType = model.getObjectByName("TextType")
		assertNotNull(textType)
		binding = textType.getTemplateBinding()
		assertNotNull(binding)
		testBindingTextType(binding)

		UmlClass uriType = model.getObjectByName("UriType")
		assertNotNull(uriType)
		binding = uriType.getTemplateBinding()
		assertNotNull(binding)
		testBindingUriType(binding)
	}

	public void testBindingDateInterval(UmlTemplateBinding binding) {
		assertNotNull(binding.getSignature())
		assertEquals(2, binding.getBindings().size())
		assertEquals("T", binding.getBindings().get(0).getFormalParameter().getName())
		assertEquals("U", binding.getBindings().get(1).getFormalParameter().getName())
		assertEquals("DATE_TIME", binding.getBindings().get(0).getActualParameter().getName())
		assertEquals("REAL", binding.getBindings().get(1).getActualParameter().getName())
		assertEquals("INTERVAL_VALUE", binding.getSignature().getOwningClass().getName())
	}

	public void testBindingTextType(UmlTemplateBinding binding) {
		assertNotNull(binding.getSignature())
		assertEquals(1, binding.getBindings().size())
		assertEquals("T", binding.getBindings().get(0).getFormalParameter().getName())
		assertEquals("TEXT", binding.getBindings().get(0).getActualParameter().getName())
		assertEquals("GenericType", binding.getSignature().getOwningClass().getName())
	}

	public void testBindingUriType(UmlTemplateBinding binding) {
		assertNotNull(binding.getSignature())
		assertEquals(1, binding.getBindings().size())
		assertEquals("T", binding.getBindings().get(0).getFormalParameter().getName())
		assertEquals("URI", binding.getBindings().get(0).getActualParameter().getName())
		assertEquals("GenericType", binding.getSignature().getOwningClass().getName())
	}

}
