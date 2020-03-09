/*
 * Remotec ZXT-600 driver for Hubitat
 * https://github.com/warrenguy/hubitat-drivers
 *
 * Copyright 2020 Warren Guy
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

metadata
{
  definition (
    name      : "Remotec ZXT-600" ,
    namespace : "warrenguy"       ,
    author    : "Warren Guy"
  )
  {
    //////////////////
    // CAPABILITIES //
    //////////////////

    capability "Thermostat"
    capability "Configuration"
    capability "Polling"
    capability "Sensor"
    capability "Battery"
    capability "Switch"
    capability "Refresh"
    capability "Actuator"
    capability "Temperature Measurement"

    //////////////
    // COMMANDS //
    //////////////

    command "configure"
    command "refresh"

    command "auto"
    command "cool"
    command "heat"
    command "fanOnly"
    command "dry"
    command "emergencyHeat"
    command "off"

    command "fanAuto"
    command "fanCirculate"
    command "fanOn"
    command "fanOff"
    command "fanLow"
    command "fanMedium"
    command "fanHigh"

    command "swingModeOn"
    command "swingModeOff"

    command "setCoolingSetpoint"   // number  coolingSetpoint
    command "setHeatingSetpoint"   // number  heatingSetpoint
    command "setThermostatFanMode" // enum    thermostatFanMode
    command "setThermostatMode"    // enum    thermostatMode

    ////////////////
    // ATTRIBUTES //
    ////////////////

    attribute "coolingSetpoint"    , "number"
    attribute "heatingSetpoint"    , "number"
    attribute "thermostatSetpoint" , "number"
    attribute "temperature"        , "number"
    attribute "swingMode"          , "enum" , [ "true" , "false" ]
    attribute "remoteCode"         , "number"
    attribute "temperatureOffset"  , "number"
    attribute "schedule"           , "json_object"

    attribute "supportedThermostatModes"    , "dynamic_enum"
    attribute "supportedThermostatFanModes" , "dynamic_enum"

    attribute "thermostatMode" , "enum" ,
      [
        "auto"           ,
        "off"            ,
        "heat"           ,
        "emergency heat" ,
        "cool"
      ]

    attribute "thermostatFanMode" , "enum" ,
      [
        "on"        ,
        "circulate" ,
        "auto"      ,
        "low"       ,
        "medium"    ,
        "high"
      ]

    /////////////////
    // FINGERPRINT //
    /////////////////

    fingerprint (
      mfr            : "5254" ,
      prod           : "0100" ,
      model          : "8490" ,
      deviceJoinName : "Remotec ZXT-600"
    )
  }

  /////////////////
  // PREFERENCES //
  /////////////////

  preferences
  {
    /////////////
    // LOGGING //
    /////////////

    input (
      name         : "debugLogEnable"       ,
      type         : "bool"                 ,
      title        : "Enable Debug Logging" ,
      defaultValue : false
    )

    input (
      name         : "infoLogEnable"       ,
      type         : "bool"                ,
      title        : "Enable Info Logging" ,
      defaultValue : true
    )

    input (
      name         : "msBetweenCommands"             ,
      type         : "number"                        ,
      range        : "0..10000"                      ,
      title        : "Milliseconds between Commands" ,
      defaultValue : 2500
    )

    input (
      name         : "setpointStepSize"   ,
      type         : "number"             ,
      range        : "0..2"               ,
      title        : "Setpoint step size" ,
      defaultValue : 0.5
    )

    input (
      name         : "coolingSetpointMin"   ,
      type         : "number"               ,
      range        : "0..32"                ,
      title        : "Min cooling setpoint" ,
      defaultValue : 18
    )

    input (
      name         : "coolingSetpointMax"   ,
      type         : "number"               ,
      range        : "0..32"                ,
      title        : "Max cooling setpoint" ,
      defaultValue : 32
    )

    input (
      name         : "heatingSetpointMin"   ,
      type         : "number"               ,
      range        : "0..32"                ,
      title        : "Min heating setpoint" ,
      defaultValue : 16
    )

    input (
      name         : "heatingSetpointMax"   ,
      type         : "number"               ,
      range        : "0..32"                ,
      title        : "Max heating setpoint" ,
      defaultValue : 32
    )

    ///////////////////////////
    // ZXT-600 CONFIGURATION //
    ///////////////////////////

    input (
      name         : "temperatureOffset"  ,
      type         : "number"             ,
      range        : "-5..5"              ,
      title        : "Temperature Offset" ,
      defaultValue : 0
    )

    input (
      name         : "remoteCode"  ,
      type         : "number"      ,
      title        : "Remote Code" ,
      defaultValue : 0
    )

    //////////////////////////////
    // supportedThermostatModes //
    //////////////////////////////

    input (
      name         : "thermostatAutoSupported" ,
      type         : "bool"                    ,
      title        : "Thermostat Mode Auto"    ,
      defaultValue : true
    )

    input (
      name         : "thermostatOffSupported" ,
      type         : "bool"                   ,
      title        : "Thermostat Mode Off"    ,
      defaultValue : true
    )

    input (
      name         : "thermostatHeatSupported" ,
      type         : "bool"                    ,
      title        : "Thermostat Mode Heat"    ,
      defaultValue : true
    )

    input (
      name         : "thermostatDrySupported" ,
      type         : "bool"                    ,
      title        : "Thermostat Mode Dry"    ,
      defaultValue : true
    )

    input (
      name         : "thermostatFanOnlySupported" ,
      type         : "bool"                    ,
      title        : "Thermostat Mode Fan Only"    ,
      defaultValue : true
    )

    input (
      name         : "thermostatEmergencyHeatSupported" ,
      type         : "bool"                             ,
      title        : "Thermostat Mode Emergency Heat"   ,
      defaultValue : false
    )

    input (
      name         : "thermostatCoolSupported" ,
      type         : "bool"                    ,
      title        : "Thermostat Mode Cool"    ,
      defaultValue : true
    )

    /////////////////////////////////
    // supportedThermostatFanModes //
    /////////////////////////////////

    input (
      name         : "fanOnSupported" ,
      type         : "bool"           ,
      title        : "Fan Mode On"    ,
      defaultValue : true
    )

    input (
      name         : "fanCirculateSupported" ,
      type         : "bool"                  ,
      title        : "Fan Mode Circulate"    ,
      defaultValue : true
    )

    input (
      name         : "fanAutoSupported" ,
      type         : "bool"             ,
      title        : "Fan Mode Auto"    ,
      defaultValue : true
    )

    input (
      name         : "fanLowSupported" ,
      type         : "bool"            ,
      title        : "Fan Mode Low"    ,
      defaultValue : true
    )

    input (
      name         : "fanMediumSupported" ,
      type         : "bool"               ,
      title        : "Fan Mode Medium"    ,
      defaultValue : true
    )

    input (
      name         : "fanHighSupported" ,
      type         : "bool"             ,
      title        : "Fan Mode High"    ,
      defaultValue : true
    )
  }
}

private Integer getREMOTE_CODE_PARAM() { 0x1B }
private Integer getREMOTE_CODE_SIZE()  {    2 }

private Integer getTEMP_OFFSET_PARAM() { 0x25 }
private Integer getTEMP_OFFSET_SIZE()  {    1 }

private Integer getSWING_MODE_PARAM()  { 0x21 }
private Integer getSWING_MODE_SIZE()   {    1 }
private Map     getSWING_MODE_VALUES() {[ "true" : 1 , "false" : 0 ]}

private Integer[] getALL_PARAMS()
{
  [
    REMOTE_CODE_PARAM ,
    TEMP_OFFSET_PARAM ,
    SWING_MODE_PARAM
  ]
}

private Map getZWAVE_COMMAND_VERSIONS()
{[

  0x20 : 1 , // Basic
  0x27 : 1 , // Switch All
  0x31 : 1 , // Sensor Multilevel
  0x40 : 2 , // Thermostat Mode
  0x43 : 2 , // Thermostat Setpoint
  0x44 : 2 , // Thermostat Fan Mode
  0x70 : 1 , // Configuration
  0x72 : 1 , // Manufacturer Specific
  0x80 : 1 , // Battery
  0x86 : 1   // Version

]}

private getTHERMOSTAT_MODE_MAP()
{[
  off            : hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_OFF            ,
  heat           : hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_HEAT           ,
  emergency_heat : hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_AUXILIARY_HEAT ,
  cool           : hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_COOL           ,
  auto           : hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_AUTO           ,
  fan            : hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_FAN_ONLY       ,
  dry            : hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport.MODE_DRY_AIR
]}

private Map getTHERMOSTAT_FAN_MODE_MAP()
{[
  auto      : hubitat.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_AUTO_LOW    ,
  circulate : hubitat.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_CIRCULATION ,
  low       : hubitat.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_LOW         ,
  medium    : hubitat.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_MEDIUM      ,
  high      : hubitat.zwave.commands.thermostatfanmodev3.ThermostatFanModeReport.FAN_MODE_HIGH
]}

private void debugLog ( message )
{
  if ( debugLogEnable )
    log.debug "${ device.displayName } DEBUG : ${ message }"
}

private void infoLog ( message )
{
  if ( infoLogEnable )
    log.info "${ device.displayName } INFO : ${ message }"
}

List refresh ()
{
  infoLog( "REFRESH" )

  List commands = [
    *ALL_PARAMS.collect { zwave.configurationV1.configurationGet( parameterNumber : it ) } ,
    zwave.sensorMultilevelV3.sensorMultilevelGet()   , // temperature
    zwave.batteryV1.batteryGet()                     ,
    zwave.thermostatModeV2.thermostatModeGet()       ,
    zwave.thermostatFanModeV3.thermostatFanModeGet() ,
    zwave.thermostatSetpointV2.thermostatSetpointGet (
      setpointType: hubitat.zwave.commands.thermostatsetpointv1.ThermostatSetpointSet.SETPOINT_TYPE_COOLING_1
    ) ,
    zwave.thermostatSetpointV2.thermostatSetpointGet (
      setpointType: hubitat.zwave.commands.thermostatsetpointv1.ThermostatSetpointSet.SETPOINT_TYPE_HEATING_1
    ) ,
    zwave.basicV1.basicGet()
  ]

  debugLog( "REFRESH : commands : ${ commands.inspect() }" )
  delayBetween( commands.collect { it.format() } , msBetweenCommands )
}

List configure ()
{
  infoLog( "CONFIGURE" )

  setSupportedThermostatModes()
  setSupportedThermostatFanModes()

  List commands = []

  if ( state.lastTemperatureOffset == null || (String) state.lastTemperatureOffset != (String) temperatureOffset || (String) device.currentState( "temperatureOffset" )?.value != (String) temperatureOffset )
    commands.addAll(*[ configureTemperatureOffset() ])

  if ( state.lastRemoteCode == null || (String) remoteCode != (String) device.currentState( "remoteCode" )?.value )
    commands.addAll(*[ configureRemoteCode() ])

  if ( commands.size < 1 ) debugLog( "CONFIGURE : no commands to run" )

  else
  {
    debugLog( "CONFIGURE COMMANDS : ${ commands.inspect() }" )
    delayBetween ( commands.collect{ it.format() } , msBetweenCommands )
  }
}

List updated ()
{
  infoLog( "UPDATED" )
  refresh()
  configure()
}

Map parse ( String description )
{
  debugLog( "PARSE : description : ${ description }" )

  def command = zwave.parse( description , ZWAVE_COMMAND_VERSIONS )
  debugLog( "PARSE : command : ${ command.inspect() }" )

  if ( command ) zwaveCommand( command )
  else [:]
}

void setSchedule ()
{
  infoLog( "setSchedule unsupported" )
}

private Map zwaveCommand ( hubitat.zwave.commands.sensormultilevelv1.SensorMultilevelReport command )
{
  if ( command.sensorType != 1 )
  {
    debugLog( "Unhandled sensorType : ${ command.inspect }" )
    return [:]
  }

  data = [
    name  : "temperature" ,
    unit  : "C"           ,
    value : command.scaledSensorValue
  ]

  sendEvent data
  data
}

private Map zwaveCommand ( hubitat.zwave.commands.batteryv1.BatteryReport command )
{
  Map data = [
    name      : "battery" ,
    displayed : false     ,
    unit      : '%'
  ]

  if ( command.batteryLevel == 0xFF )
  {
    data.value           = "0"
    data.descriptionText = "${ device.displayName } LOW BATTERY"
  }

  else if ( command.batteryLevel > 0 )
    data.value = (String) command.batteryLevel

  else
  {
    data.value           = "100"
    data.descriptionText = "${ device.displayName } on USB power"
  }

  sendEvent data
  data
}

private Map zwaveCommand ( hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport command )
{
  Map data = [
    name  : "thermostatMode" ,
    value : THERMOSTAT_MODE_MAP.find { it.value == command.mode }?.key
  ]

  sendEvent data
  data
}

private Map zwaveCommand ( hubitat.zwave.commands.thermostatfanmodev2.ThermostatFanModeReport command )
{
  Map data = [
    name  : "thermostatFanMode" ,
    value : THERMOSTAT_FAN_MODE_MAP.find { it.value == command.fanMode }?.key
  ]

  sendEvent data
  data
}

private Map getSETPOINT_TYPE_MAP()
{[
  cooling : hubitat.zwave.commands.thermostatsetpointv1.ThermostatSetpointReport.SETPOINT_TYPE_COOLING_1 ,
  heating : hubitat.zwave.commands.thermostatsetpointv1.ThermostatSetpointReport.SETPOINT_TYPE_HEATING_1
]}

private Map zwaveCommand ( hubitat.zwave.commands.thermostatsetpointv1.ThermostatSetpointReport command )
{
  debugLog( "ThermostatSetpointReport : ${ command.inspect() }" )

  Map data = [
    value : command.scaledValue ,
    unit  : "C"
  ]

  switch ( SETPOINT_TYPE_MAP.find{ it.value == command.setpointType }?.key )
  {

    case "cooling" :
      data.name = "coolingSetpoint"
      break

    case "heating" :
      data.name = "heatingSetpoint"
      break

    default :
      log.warn( "Received unknown ThermostSetpointReport setpointType : ${ command.inspect() }" )
      return []

  }

  sendEvent data
  data
}

private Map zwaveCommand ( hubitat.zwave.commands.applicationstatusv1.ApplicationBusy command )
{
  debugLog( "ApplicationBusy : ${ command.inspect() }" )
  [:]
}

private Map zwaveCommand ( hubitat.zwave.commands.basicv1.BasicReport command )
{
  debugLog( "BasicReport : ${ command.inspect() }" )
  [:]
}

private Map zwaveCommand ( hubitat.zwave.commands.configurationv1.ConfigurationReport command )
{
  Map data = [:]

  debugLog( "getPayload : ${ command.getPayload() }" )

  switch ( command.parameterNumber )
  {
    case REMOTE_CODE_PARAM :
      data.name      = "remoteCode"
      data.displayed = false
      data.value     = parseRemoteBytes( (Short[]) command.configurationValue )
      break

    case TEMP_OFFSET_PARAM :
      data.name      = "temperatureOffset"
      data.displayed = false
      data.value     = command.configurationValue[ 0 ]
      break

    case SWING_MODE_PARAM :
      data.name      = "swingMode"
      data.displayed = false
      data.value     = command.configurationValue[ 0 ] == 1 ? "true" : "false"
      break

    default :
      debugLog( "Unhandled command : ${ command.inspect() }" )
  }

  debugLog( "zwaveCommand : data : ${ data.inspect() }" )

  if ( data != [:] ) sendEvent data
  data
}

private void setSupportedThermostatModes ()
{
  List modes = []
  if ( thermostatAutoSupported          ) modes << "auto"
  if ( thermostatOffSupported           ) modes << "off"
  if ( thermostatHeatSupported          ) modes << "heat"
  if ( thermostatEmergencyHeatSupported ) modes << "emergency heat"
  if ( thermostatFanOnlySupported       ) modes << "fan"
  if ( thermostatDrySupported           ) modes << "dry"
  if ( thermostatCoolSupported          ) modes << "cool"

  sendEvent( name : "supportedThermostatModes" , value : modes )
}

private void setSupportedThermostatFanModes ()
{
  List modes = []
  if ( fanOnSupported        ) modes << "on"
  if ( fanAutoSupported      ) modes << "auto"
  if ( fanCirculateSupported ) modes << "circulate"
  if ( fanLowSupported       ) modes << "low"
  if ( fanMediumSupported    ) modes << "medium"
  if ( fanHighSupported      ) modes << "high"

  sendEvent( name : "supportedThermostatFanModes" , value : modes )
}

private List configureTemperatureOffset ()
{
  Integer val = temperatureOffset == null ? 0 : (Integer) temperatureOffset
  if ( val < 0 ) val = 256 + val

  debugLog( "setTemperatureOffset : ${ temperatureOffset } (${ val })" )
  state.lastTemperatureOffset = temperatureOffset
  sendEvent( name : "temperatureOffset" , value : temperatureOffset )

  configureCommands( TEMP_OFFSET_PARAM , TEMP_OFFSET_SIZE , [ val ] )
}

private List configureRemoteCode ()
{
  if ( remoteCode == null )
  {
    log.warn "remoteCode not set, not setting on device"
    return []
  }

  Short[] codeBytes = parseRemoteCode( (Integer) remoteCode )
  debugLog( "setRemoteCode : ${ remoteCode } (${ codeBytes.inspect() })" )

  state.lastRemoteCode = remoteCode
  sendEvent( name : "remoteCode" , value : remoteCode )

  configureCommands( REMOTE_CODE_PARAM , REMOTE_CODE_SIZE , (List) codeBytes )
}

private List configureSwingMode ()
{
  String cur = device.currentState( "swingMode" )?.value
  if ( cur == null ) cur = "false"

  Integer val = SWING_MODE_VALUES[ cur ]

  if ( val == null )
  {
    log.warn "invalid value '${ cur }' for swingMode"
    return []
  }

  debugLog( "setSwingMode : ${ cur } (${ val })" )

  state.lastSwingMode = cur
  sendEvent( name : "swingMode" , value : cur )

  List commands = configureCommands( SWING_MODE_PARAM , SWING_MODE_SIZE , [ val ] )
  delayBetween( commands.collect { it.format() } , msBetweenCommands )
}

List swingModeOn  () { setSwingMode( "true"  ) }
List swingModeOff () { setSwingMode( "false" ) }

private List setSwingMode ( String val )
{
  sendEvent( name : "swingMode" , value : val )
  pauseExecution( 500 )
  configureSwingMode()
}

List setCoolingSetpoint ()
{
  setCoolingSetpoint( new BigDecimal( device.currentState( "coolingSetpoint" )?.value ) )
}

List setCoolingSetpoint ( BigDecimal value )
{
  if ( value.remainder( (BigDecimal) setpointStepSize ) != 0 )
  {
    if ( value < state.lastCoolingSetpoint )
      value = state.lastCoolingSetpoint - setpointStepSize

    else if ( value > state.lastCoolingSetpoint )
      value = state.lastCoolingSetpoint + setpointStepSize
  }

  value = Math.max( (BigDecimal) coolingSetpointMin , value )
  value = Math.min( (BigDecimal) coolingSetpointMax , value )

  switch ( device.currentState( "thermostatMode" )?.value )
  {
    case "cool" :
    case "auto" :
      infoLog( "Setting coolingSetpoint ${ value }" )
      state.lastCoolingSetpoint = value
      sendEvent( name : "coolingSetpoint" , value : value , unit : "C" )
      pauseExecution( 500 )
      setThermostatMode( device.currentState( "thermostatMode" ).value )
      break

    default :
      sendEvent( name : "coolingSetpoint" , value : value , unit : "C" )
      state.lastHeatingSetpoint = value
      debugLog( "Not setting coolingSetpoint (mode is ${ device.currentState( "thermostatMode" )?.value })" )
      break
  }
}


List setHeatingSetpoint ()
{
  setHeatingSetpoint( new BigDecimal( device.currentState( "heatingSetpoint" )?.value ) )
}

List setheatingSetpoint ( BigDecimal value )
{
  if ( value.remainder( (BigDecimal) setpointStepSize ) != 0 )
  {
    if ( value < state.lastheatingSetpoint )
      value = state.lastheatingSetpoint - setpointStepSize

    else if ( value > state.lastheatingSetpoint )
      value = state.lastheatingSetpoint + setpointStepSize
  }

  value = Math.max( (BigDecimal) heatingSetpointMin , value )
  value = Math.min( (BigDecimal) heatingSetpointMax , value )

  switch ( device.currentState( "thermostatMode" )?.value )
  {
    case "cool" :
    case "auto" :
      infoLog( "Setting heatingSetpoint ${ value }" )
      state.lastheatingSetpoint = value
      sendEvent( name : "heatingSetpoint" , value : value , unit : "C" )
      pauseExecution( 500 )
      setThermostatMode( device.currentState( "thermostatMode" ).value )
      break

    default :
      sendEvent( name : "heatingSetpoint" , value : value , unit : "C" )
      state.lastHeatingSetpoint = value
      debugLog( "Not setting heatingSetpoint (mode is ${ device.currentState( "thermostatMode" )?.value })" )
      break
  }
}

List fanOn        () { setThermostatFanMode( "on"        ) }
List fanOff       () { setThermostatFanMode( "off"       ) }
List fanLow       () { setThermostatFanMode( "low"       ) }
List fanMedium    () { setThermostatFanMode( "medium"    ) }
List fanHigh      () { setThermostatFanMode( "high"      ) }
List fanAuto      () { setThermostatFanMode( "auto"      ) }
List fanCirculate () { setThermostatFanMode( "circulate" ) }

List setThermostatFanMode () { fanOn() }

List setThermostatFanMode ( String mode )
{
  mode = mode.trim()

  if ( !device.currentState( "supportedThermostatFanModes" )?.value?.contains( mode ) )
  {
    debugLog( "Unsupported thermostat fan mode : ${ mode }" )
    return []
  }

  if ( mode == "on" && state.lastThermostatFanMode != null )
    mode = state.lastThermostatFanMode

  List commands = [
    zwave.thermostatFanModeV2.thermostatFanModeSet (
      fanMode: THERMOSTAT_FAN_MODE_MAP[ mode ]
    ) ,
    zwave.thermostatFanModeV2.thermostatFanModeGet()
  ]

  debugLog( "setThermostatFanMode : ${ commands.inspect() }" )
  state.lastThermostatFanMode = mode
  delayBetween( commands.collect { it.format() } , msBetweenCommands )
}

List on            () { setThermostatMode( "on"             ) }
List off           () { setThermostatMode( "off"            ) }
List auto          () { setThermostatMode( "auto"           ) }
List fanOnly       () { setThermostatMode( "fan"            ) }
List dry           () { setThermostatMode( "dry"            ) }
List emergencyHeat () { setThermostatMode( "emergency_heat" ) }
List heat          () { setThermostatMode( "heat"           ) }
List cool          () { setThermostatMode( "cool"           ) }

List setThermostatMode () { on() }

List setThermostatMode ( String mode )
{
  if ( mode == "on" && state.lastThermostatMode != null )
    mode = state.lastThermostatMode

  if ( !device.currentState( "supportedThermostatModes" )?.value?.contains( mode ) )
  {
    debugLog( "Unsupported thermostat mode : ${ mode }" )
    return []
  }

  List commands = []
  BigDecimal setTemp = null
  def setpointMode = null

  switch ( mode )
  {
    case "cool" :
      setTemp      = ( new BigDecimal( device.currentState( "coolingSetpoint" )?.value ) ).setScale( 0 )
      setpointMode = hubitat.zwave.commands.thermostatsetpointv2.ThermostatSetpointSet.SETPOINT_TYPE_COOLING_1
      break

    case "heat" :
      setTemp      = ( new BigDecimal( device.currentState( "heatingSetpoint" )?.value ) ).setScale( 0 )
      setpointMode = hubitat.zwave.commands.thermostatsetpointv2.ThermostatSetpointSet.SETPOINT_TYPE_HEATING_1
      break

    case "emergency_heat" :
    case "dry"  :
    case "fan"  :
    case "auto" :
    case "off"  :
      break

    default :
      debugLog ( "Unknown thermostatMode : ${ mode }" )
      return false
  }

  commands.addAll ([
    zwave.thermostatModeV2.thermostatModeSet( mode: THERMOSTAT_MODE_MAP[ mode ] ) ,
    zwave.thermostatModeV2.thermostatModeGet() ,
    zwave.basicV1.basicGet()
  ])

  if ( setTemp != null )
    commands.addAll ([
      zwave.thermostatSetpointV2.thermostatSetpointSet (
        setpointType : setpointMode      ,
        scale        : setTemp.scale     ,
        precision    : setTemp.precision ,
        scaledValue  : setTemp
      ) ,
      zwave.thermostatSetpointV2.thermostatSetpointGet (
        setpointType: setpointMode
      )
    ])

  debugLog( "setThermostatMode : ${ commands.inspect() }" )
  state.lastThermostatMode = mode
  delayBetween( commands.collect { it.format() } , msBetweenCommands )
}

private List configureCommands ( Integer param , Integer size , List val )
{[
  zwave.configurationV1.configurationSet (
    configurationValue : val   ,
    parameterNumber    : param ,
    size               : size
  ) ,
  zwave.configurationV1.configurationGet( parameterNumber : param )
]}

private Integer parseRemoteBytes ( Short[] codeBytes )
{
  ( ( (Integer) codeBytes[ 0 ] ) << 8 ) + ( (Integer) codeBytes[ 1 ] )
}

private Short[] parseRemoteCode ( Integer code )
{
  Short[] codeBytes = [ null , null ]
  codeBytes[ 1 ] = code & 0xFF
  codeBytes[ 0 ] = ( code >> 8 ) & 0xFF

  codeBytes
}
