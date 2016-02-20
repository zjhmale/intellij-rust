package org.rust.cargo.project.settings.controls

import com.intellij.openapi.externalSystem.service.settings.AbstractExternalProjectSettingsControl
import com.intellij.openapi.externalSystem.util.ExternalSystemUiUtil
import com.intellij.openapi.externalSystem.util.PaintAwarePanel
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.roots.ui.configuration.JdkComboBox
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel
import com.intellij.openapi.util.Conditions
import com.intellij.ui.components.JBLabel
import org.rust.cargo.project.RustSdkType
import org.rust.cargo.project.settings.CargoProjectSettings
import java.awt.GridBagConstraints
import javax.swing.JButton

class CargoProjectSettingsControl(settings: CargoProjectSettings)
    : AbstractExternalProjectSettingsControl<CargoProjectSettings>(settings) {

    private lateinit var sdkComboBox: JdkComboBox

    init  {
        println("init")
    }

    override fun fillExtraControls(content: PaintAwarePanel, indentLevel: Int) {
        println("fill extra controlls")
        val sdkModel = ProjectSdksModel().apply { reset(project) }
        sdkComboBox = JdkComboBox(sdkModel, Conditions.equalTo(RustSdkType.INSTANCE)).apply {
            setSetupButton(JButton("New..."), project, sdkModel, null, null, false)
        }

        with(content) {
            add(JBLabel("Rust SDK:"), ExternalSystemUiUtil.getLabelConstraints(indentLevel))
            add(sdkComboBox, ExternalSystemUiUtil.getFillLineConstraints(0).coverLine(GridBagConstraints.RELATIVE))
            add(sdkComboBox.setUpButton, ExternalSystemUiUtil.getFillLineConstraints(0))
        }
    }

    override fun resetExtraSettings(isDefaultModuleCreation: Boolean) {
        // This does not work ;(
        // If you open import dialog `n` times in a row, you'll see `n` "no SDK" options
        sdkComboBox.reloadModel(JdkComboBox.NoneJdkComboBoxItem(), project)
    }

    override fun validate(settings: CargoProjectSettings): Boolean {
        println("validate")
        if (currentCargoHome == null) {
            throw ConfigurationException("Select a Rust SDK")
        }
        return true
    }

    override fun applyExtraSettings(settings: CargoProjectSettings) {
        settings.cargoHome = currentCargoHome
    }

    override fun isExtraSettingModified(): Boolean =
        initialSettings.cargoHome != currentCargoHome

    private val currentCargoHome: String? get() = sdkComboBox.selectedJdk?.homePath
}
