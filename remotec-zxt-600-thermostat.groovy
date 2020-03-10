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

    command "fanLow"
    command "fanMedium"
    command "fanHigh"

    command "swingModeOn"
    command "swingModeOff"

    ////////////////
    // ATTRIBUTES //
    ////////////////

    attribute "coolingSetpoint"    , "number"
    attribute "heatingSetpoint"    , "number"
    attribute "thermostatSetpoint" , "number"
    attribute "temperature"        , "number"
    attribute "swingMode"          , "enum" , [ "true" , "false" ]
    attribute "remoteCode"         , "number"
    attribute "reportTempLevel"    , "string"
    attribute "reportTime"         , "string"
    attribute "internalInfrared"   , "boolean"
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
      range        : "1..2"               ,
      title        : "Setpoint step size" ,
      defaultValue : 1
    )

    input (
      name         : "coolingSetpointMin"   ,
      type         : "number"               ,
      range        : "17..30"               ,
      title        : "Min cooling setpoint" ,
      defaultValue : 17
    )

    input (
      name         : "coolingSetpointMax"   ,
      type         : "number"               ,
      range        : "17..30"               ,
      title        : "Max cooling setpoint" ,
      defaultValue : 30
    )

    input (
      name         : "heatingSetpointMin"   ,
      type         : "number"               ,
      range        : "17..30"               ,
      title        : "Min heating setpoint" ,
      defaultValue : 17
    )

    input (
      name         : "heatingSetpointMax"   ,
      type         : "number"               ,
      range        : "17..30"               ,
      title        : "Max heating setpoint" ,
      defaultValue : 30
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

    input (
      name         : "internalInfrared"   ,
      type         : "bool"               ,
      title        : "Enable built-in IR" ,
      defaultValue : true
    )

    input (
      name         : "reportTempLevel" ,
      type         : "enum"            ,
      options      : REPORT_TEMP_VALUES.keySet().toList()     ,
      title        : "Temperature change reporting threshold" ,
      defaultValue : "off"
    )

    input (
      name         : "reportTime" ,
      type         : "enum"       ,
      options      : REPORT_TIME_VALUES.keySet().toList() ,
      title        : "Time reporting threshold"           ,
      defaultValue : "1 hour"
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
      type         : "bool"                   ,
      title        : "Thermostat Mode Dry"    ,
      defaultValue : true
    )

    input (
      name         : "thermostatFanOnlySupported" ,
      type         : "bool"                       ,
      title        : "Thermostat Mode Fan Only"   ,
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
private Map     getSWING_MODE_VALUES()
{[
  "true"  : 0x01 ,
  "false" : 0x00
]}

private Integer getINTERNAL_IR_PARAM() { 0x20 }
private Integer getINTERNAL_IR_SIZE()  {    1 }
private Map     getINTERNAL_IR_VALUES()
{[
  "true"  : 0xFF ,
  "false" : 0x00
]}

private Integer getREPORT_TEMP_PARAM() { 0x1E }
private Integer getREPORT_TEMP_SIZE()  {    1 }
private Map     getREPORT_TEMP_VALUES()
{[
  "off" : 0x00 ,
  "0.5" : 0x01 ,
  "1.0" : 0x02 ,
  "1.5" : 0x03 ,
  "2.0" : 0x04 ,
  "2.5" : 0x05 ,
  "3.0" : 0x06 ,
  "3.5" : 0x07 ,
  "4.0" : 0x08
]}

private Integer getREPORT_TIME_PARAM() { 0x22 }
private Integer getREPORT_TIME_SIZE()  {    1 }
private Map     getREPORT_TIME_VALUES()
{[
  "1 hour"  : 0x01 ,
  "2 hours" : 0x02 ,
  "3 hours" : 0x03 ,
  "4 hours" : 0x04 ,
  "5 hours" : 0x05 ,
  "6 hours" : 0x06 ,
  "7 hours" : 0x07 ,
  "8 hours" : 0x08
]}

private Integer[] getALL_PARAMS()
{[
  REMOTE_CODE_PARAM ,
  TEMP_OFFSET_PARAM ,
  SWING_MODE_PARAM  ,
  INTERNAL_IR_PARAM ,
  REPORT_TEMP_PARAM ,
  REPORT_TIME_PARAM
]}

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

private Map getTHERMOSTAT_MODE_MAP()
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

private Map getCONFIG_STATE()
{[
  "temperatureOffset" : ( (String) temperatureOffset   ) == ( (String) device.currentState( "temperatureOffset" )?.value ) ,
  "remoteCode"        : ( (String) remoteCode          ) == ( (String) device.currentState( "remoteCode"        )?.value ) ,
  "swingMode"         : ( (String) state.lastSwingMode ) == ( (String) device.currentState( "swingMode"         )?.value ) ,
  "internalInfrared"  : ( (String) internalInfrared    ) == ( (String) device.currentState( "internalInfrared"  )?.value ) ,
  "reportTempLevel"   : ( (String) reportTempLevel     ) == ( (String) device.currentState( "reportTempLevel"   )?.value ) ,
  "reportTime"        : ( (String) reportTime          ) == ( (String) device.currentState( "reportTime"        )?.value ) ,
]}

List configure ()
{
  infoLog( "CONFIGURE" )

  setSupportedThermostatModes()
  setSupportedThermostatFanModes()

  List commands    = []
  Map  configState = CONFIG_STATE

  if ( !CONFIG_STATE.temperatureOffset ) commands.addAll(*[ configureTemperatureOffset() ])
  if ( !CONFIG_STATE.remoteCode        ) commands.addAll(*[ configureRemoteCode()        ])
  if ( !CONFIG_STATE.swingMode         ) commands.addAll(*[ configureSwingMode()         ])
  if ( !CONFIG_STATE.internalInfrared  ) commands.addAll(*[ configureInternalInfrared()  ])
  if ( !CONFIG_STATE.reportTempLevel   ) commands.addAll(*[ configureReportTempLevel()   ])
  if ( !CONFIG_STATE.reportTime        ) commands.addAll(*[ configureReportTime()        ])

  if ( commands.size < 1 )
    debugLog( "CONFIGURE : no commands to run" )

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

    case INTERNAL_IR_PARAM :
      data.name      = "internalInfrared"
      data.displayed = false
      data.value     = INTERNAL_IR_VALUES.find { it.value == command.configurationValue[ 0 ] }?.key
      break

    case REPORT_TIME_PARAM :
      data.name      = "reportTime"
      data.displayed = false
      data.value     = REPORT_TIME_VALUES.find { it.value == command.configurationValue[ 0 ] }?.key
      break

    case REPORT_TEMP_PARAM :
      data.name      = "reportTempLevel"
      data.displayed = false
      data.value     = REPORT_TEMP_VALUES.find { it.value == command.configurationValue[ 0 ] }?.key
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

  configureCommands( SWING_MODE_PARAM , SWING_MODE_SIZE , [ val ] )
}

List swingModeOn  () { setSwingMode( "true"  ) }
List swingModeOff () { setSwingMode( "false" ) }

private List setSwingMode ( String val )
{
  sendEvent( name : "swingMode" , value : val )
  pauseExecution( 500 )
  configure()
}

private List configureInternalInfrared ()
{
  String  opt = internalInfrared == null ? "true" : internalInfrared
  Integer val = INTERNAL_IR_VALUES[ opt ]

  if ( val == null )
  {
    log.warn "Invalid value for internalInfrared : ${ opt }"
    return []
  }

  sendEvent( name : "internalInfrared" , value : opt , displayed : false )
  pauseExecution( 500 )

  debugLog( "setInternalInfrared: ${ opt } (${ val })" )
  state.lastInternalInfrared = internalInfrared

  configureCommands( INTERNAL_IR_PARAM , INTERNAL_IR_SIZE , [ val ] )
}

private List configureReportTempLevel ()
{
  String  opt = reportTempLevel == null ? "true" : reportTempLevel
  Integer val = REPORT_TEMP_VALUES[ opt ]

  if ( val == null )
  {
    log.warn "Invalid value for reportTempLevel : ${ opt }"
    return []
  }

  sendEvent( name : "reportTempLevel" , value : opt , displayed : false )
  pauseExecution( 500 )

  debugLog( "setReportTempLevel: ${ opt } (${ val })" )
  state.lastReportTempLevel = reportTempLevel

  configureCommands( REPORT_TEMP_PARAM , REPORT_TEMP_SIZE , [ val ] )
}

private List configureReportTime ()
{
  String  opt = reportTime == null ? "true" : reportTime
  Integer val = REPORT_TIME_VALUES[ opt ]

  if ( val == null )
  {
    log.warn "Invalid value for reportTime : ${ opt }"
    return []
  }

  sendEvent( name : "reportTime" , value : opt , displayed : false )
  pauseExecution( 500 )

  debugLog( "setReportTime: ${ opt } (${ val })" )
  state.lastReportTime = reportTime

  configureCommands( REPORT_TIME_PARAM , REPORT_TIME_SIZE , [ val ] )
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
    case "heat" :
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
