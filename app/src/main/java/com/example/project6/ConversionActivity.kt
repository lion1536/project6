package com.example.project6

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast

class ConversionActivity : AppCompatActivity() {

    private lateinit var inputValue: EditText
    private lateinit var outputValue: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner

    companion object {
        const val EXTRA_CATEGORY = "category"
    }

    data class Unit(val name: String, val factorToBase: Double, val offset: Double = 0.0)
    data class UnitCategory(val name: String, val units: List<Unit>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conversion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Atribuindo as views corretamente
        inputValue = findViewById(R.id.editTextNumber3) // EditText para input
        outputValue = findViewById(R.id.editTextNumber4) // EditText para mostrar o resultado
        spinnerFrom = findViewById(R.id.spinner2) // Spinner para selecionar a unidade de origem
        spinnerTo = findViewById(R.id.spinner3) // Spinner para selecionar a unidade de destino


        val unitConverter = UnitConverter()
        val categoryName = intent.getStringExtra(EXTRA_CATEGORY)
        val selectedCategory = unitConverter.categories.find { it.name == categoryName }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, selectedCategory?.units?.map { it.name } ?: emptyList())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.adapter = categoryAdapter
        spinnerTo.adapter = categoryAdapter


        // Função para realizar a conversão
        fun convertUnits() {
            val inputText = inputValue.text.toString()
            if (inputText.isNotEmpty()) {
                try {
                    val input = inputText.toDouble()
                    val unitFromName = spinnerFrom.selectedItem.toString()
                    val unitToName = spinnerTo.selectedItem.toString()

                    val category = unitConverter.categories.find { it.name == categoryName }
                    val unitFrom = category?.units?.find { it.name == unitFromName }
                    val unitTo = category?.units?.find { it.name == unitToName }

                    if (unitFrom != null && unitTo != null) {
                        val baseValue = (input + unitFrom.offset) * unitFrom.factorToBase
                        val convertedValue = (baseValue / unitTo.factorToBase) - unitTo.offset
                        outputValue.setText(convertedValue.toString())

                    } else {
                        Toast.makeText(this, "Erro ao encontrar as unidades", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Erro na conversão", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, insira um valor válido", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener para o campo de entrada de valor, chama a conversão
        inputValue.setOnEditorActionListener { _, _, _ ->
            convertUnits() // Chama a conversão quando o valor de input for alterado
            true
        }
    }

        class UnitConverter {
            val categories = listOf(
                UnitCategory(
                    "Mass",
                    listOf(
                        Unit("Tonne", 1000.0), // 1 tonne = 1000 kg
                        Unit("Kilogram", 1.0), // Base unit
                        Unit("Gram", 0.001), // 1 gram = 0.001 kg
                        Unit("Milligram", 0.000001), // 1 milligram = 0.000001 kg
                        Unit("Microgram", 0.000000001), // 1 microgram = 0.000000001 kg
                        Unit("Imperial ton", 1016.0469088), // 1 imperial ton = 1016.0469088 kg
                        Unit("US ton", 907.18474), // 1 US ton = 907.18474 kg
                        Unit("Stone", 6.35029318), // 1 stone = 6.35029318 kg
                        Unit("Pound", 0.45359237), // 1 pound = 0.45359237 kg
                        Unit("Ounce", 0.028349523125) // 1 ounce = 0.028349523125 kg
                    )
                ),
                UnitCategory(
                    "Length",
                    listOf(
                        Unit("Meter", 1.0), // Base unit
                        Unit("Kilometer", 1000.0), // 1 kilometer = 1000 meters
                        Unit("Centimeter", 0.01), // 1 centimeter = 0.01 meters
                        Unit("Millimeter", 0.001), // 1 millimeter = 0.001 meters
                        Unit("Micrometer", 0.000001), // 1 micrometer = 0.000001 meters
                        Unit("Nanometer", 0.000000001), // 1 nanometer = 0.000000001 meters
                        Unit("Mile", 1609.344), // 1 mile = 1609.344 meters
                        Unit("Yard", 0.9144), // 1 yard = 0.9144 meters
                        Unit("Foot", 0.3048), // 1 foot = 0.3048 meters
                        Unit("Inch", 0.0254), // 1 inch = 0.0254 meters
                        Unit("Nautical mile", 1852.0) // 1 nautical mile = 1852.0 meters
                    )
                ),
                UnitCategory(
                    "Temperature",
                    listOf(
                        Unit("Celsius", 1.0, 0.0),           // base
                        Unit("Fahrenheit", 0.555555, -32.0), // (F - 32) * 5/9
                        Unit("Kelvin", 1.0, -273.15)          // (K - 273.15) * 1
                    )
                ),
                UnitCategory(
                    "Digital Storage",
                    listOf(
                        Unit("Bit", 1.0), // Base unit
                        Unit("Kilobit", 1000.0), // 1 kilobit = 1000 bits
                        Unit("Kibibit", 1024.0), // 1 kibibit = 1024 bits
                        Unit("Megabit", 1000000.0), // 1 megabit = 1000000 bits
                        Unit("Mebibit", 1048576.0), // 1 mebibit = 1048576 bits
                        Unit("Gigabit", 1000000000.0), // 1 gigabit = 1000000000 bits
                        Unit("Gibibit", 1073741824.0), // 1 gibibit = 1073741824 bits
                        Unit("Terabit", 1000000000000.0), // 1 terabit = 1000000000000 bits
                        Unit("Tebibit", 1099511627776.0), // 1 tebibit = 1099511627776 bits
                        Unit("Petabit", 1000000000000000.0), // 1 petabit = 1000000000000000 bits
                        Unit("Pebibit", 1125899906842624.0), // 1 pebibit = 1125899906842624 bits
                        Unit("Byte", 8.0), // 1 byte = 8 bits
                        Unit("Kilobyte", 8000.0), // 1 kilobyte = 8000 bits
                        Unit("Kibibyte", 8192.0), // 1 kibibyte = 8192 bits
                        Unit("Megabyte", 8000000.0), // 1 megabyte = 8000000 bits
                        Unit("Mebibyte", 8388608.0), // 1 mebibyte = 8388608 bits
                        Unit("Gigabyte", 8000000000.0), // 1 gigabyte = 8000000000 bits
                        Unit("Gibibyte", 8589934592.0), // 1 gibibyte = 8589934592 bits
                        Unit("Terabyte", 8000000000000.0), // 1 terabyte = 8000000000000 bits
                        Unit("Tebibyte", 8796093022208.0), // 1 tebibyte = 8796093022208 bits
                        Unit("Petabyte", 8000000000000000.0), // 1 petabyte = 8000000000000000 bits
                        Unit("Pebibyte", 9007199254740992.0) // 1 pebibyte = 9007199254740992 bits
                    )
                ),
                UnitCategory(
                    "Frequency",
                    listOf(
                        Unit("Hertz", 1.0),
                        Unit("Kilohertz", 1e3),
                        Unit("Megahertz", 1e6),
                        Unit("Gigahertz", 1e9)
                    )
                ),
                UnitCategory(
                  "Speed",
                    listOf(
                        Unit("Meter per second", 1.0),
                        Unit("Kilometer per hour", 0.277778),
                        Unit("Foot per second", 0.3048),
                        Unit("Mile per hour", 0.44704),
                        Unit("Knot", 0.514444)
                    )
                ),
                UnitCategory(
                    "Time",
                    listOf(
                        Unit("Nanosecond", 1e-9),
                        Unit("Microsecond", 1e-6),
                        Unit("Millisecond", 1e-3),
                        Unit("Second", 1.0),
                        Unit("Minute", 60.0),
                        Unit("Hour", 3600.0),
                        Unit("Day", 86400.0),
                        Unit("Week", 604800.0),
                        Unit("Month", 2629800.0), // Aproximadamente 30.44 dias
                        Unit("Year", 31557600.0), // Aproximadamente 365.25 dias
                        Unit("Decade", 315576000.0),
                        Unit("Century", 3155760000.0)
                    )
            ),
                UnitCategory(
                    "Area",
                    listOf(
                        Unit("Square kilometer", 1e6),
                        Unit("Square meter", 1.0),
                        Unit("Square mile", 2.59e6),
                        Unit("Square yard", 0.83612736),
                        Unit("Square foot", 0.09290304),
                        Unit("Square inch", 0.00064516),
                        Unit("Hectare", 10000.0),
                        Unit("Acre", 4046.8564224)
                    )
                ),
                UnitCategory(
                    "Data Transfer Rate",
                    listOf(
                        Unit("Bit per second", 1.0),
                        Unit("Kilobit per second", 1e3),
                        Unit("Kilobyte per second", 8e3),
                        Unit("Kibibit per second", 1024.0),
                        Unit("Megabit per second", 1e6),
                        Unit("Megabyte per second", 8e6),
                        Unit("Mebibit per second", 1_048_576.0),
                        Unit("Gigabit per second", 1e9),
                        Unit("Gigabyte per second", 8e9),
                        Unit("Gibibit per second", 1_073_741_824.0),
                        Unit("Terabit per second", 1e12),
                        Unit("Terabyte per second", 8e12),
                        Unit("Tebibit per second", 1_099_511_627_776.0)
                    )
                ),
                UnitCategory(
                    "Energy",
                    listOf(
                        Unit("Joule", 1.0),
                        Unit("Kilojoule", 1_000.0),
                        Unit("Gram calorie", 4.184),
                        Unit("Kilocalorie", 4184.0),
                        Unit("Watt hour", 3600.0),
                        Unit("Kilowatt-hour", 3_600_000.0),
                        Unit("Electronvolt", 1.602176634e-19),
                        Unit("British thermal unit", 1055.06),
                        Unit("US therm", 1.055e8),
                        Unit("Foot pound", 1.35582)
                    )
                ),
                UnitCategory(
                    "Fuel Economy",
                    listOf(
                        Unit("Mile per US gallon", 0.425144),
                        Unit("Mile per gallon", 0.354006), // Imperial gallon
                        Unit("Kilometer per liter", 1.0),
                        Unit("Liter per 100 kilometers", 100.0) // Esse exige lógica especial: `100 / valor`
                    )
                ),
                UnitCategory(
                    "Plane Angle",
                    listOf(
                        Unit("Arcsecond", Math.PI / (180.0 * 3600.0)),
                        Unit("Degree", Math.PI / 180.0),
                        Unit("Gradian", Math.PI / 200.0),
                        Unit("Milliradian", 0.001),
                        Unit("Minute of arc", Math.PI / (180.0 * 60.0)),
                        Unit("Radian", 1.0)
                    )
                ),
                UnitCategory(
                    "Pressure",
                    listOf(
                        Unit("Bar", 100_000.0),
                        Unit("Pascal", 1.0),
                        Unit("Pound per square inch", 6894.76),
                        Unit("Standard atmosphere", 101325.0),
                        Unit("Torr", 133.322)
                    )
                ),
                UnitCategory(
                    "Volume",
                    listOf(
                        Unit("US liquid gallon", 0.00378541),
                        Unit("US liquid quart", 0.000946353),
                        Unit("US liquid pint", 0.000473176),
                        Unit("US legal cup", 0.00024),
                        Unit("US fluid ounce", 2.9574e-5),
                        Unit("US tablespoon", 1.4787e-5),
                        Unit("US teaspoon", 4.9289e-6),
                        Unit("Cubic meter", 1.0),
                        Unit("Liter", 0.001),
                        Unit("Milliliter", 1e-6),
                        Unit("Imperial gallon", 0.00454609),
                        Unit("Imperial quart", 0.00113652),
                        Unit("Imperial pint", 0.000568261),
                        Unit("Imperial cup", 0.000284131),
                        Unit("Imperial fluid ounce", 2.8413e-5),
                        Unit("Imperial tablespoon", 1.7758e-5),
                        Unit("Imperial teaspoon", 5.9194e-6),
                        Unit("Cubic centimeter", 1e-6),
                        Unit("Cubic foot", 0.0283168),
                        Unit("Cubic inch", 1.6387e-5)
                    )
                )
            )
        }
    }