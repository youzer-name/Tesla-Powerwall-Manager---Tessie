/**
 *  Tesla Powerwall Manager 
 * 
 *  Copyright 2019-2024 DarwinsDen.com
 *  
 *  ****** WARNING ****** USE AT YOUR OWN RISK!
 *  This software was developed in the hopes that it will be useful to others, however, 
 *  it is beta software and may have unforeseen side effects to your equipment and related accounts.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 * 
 */

String version() {
    return "v0.3.83.20240216"
}

/*  
 * 17-Sep-2024 >>> v0.0.1.20240917 - Fork code to implement Tessie API access to Tesla Fleet API.
 * 05-Apr-2024 >>> v0.3.84.20240405 - Correct for apparent Tesla server API change relating to Battery Percentage. Thanks to @NeilR.
 * 16-Feb-2024 >>> v0.3.83.20240216 - Re-add Backup-Only mode with deprecated warning.
 * 16-Feb-2024 >>> v0.3.82.20240216 - Add battery capacity/total energy - thank you D. Mills for the code snippet. Added grid charging enable/
 *                                    disable to scheduler. Removed Tesla deprecated calls from scheduler (set TOU-Strategy, set Backup-Only mode)
 * 26-Jan-2024 >>> v0.3.81.20240126 - Update for Tesla API auth change. Add commands for energy export mode and grid charging.
 * 23-Oct-2023 >>> v0.3.80.20231023 - Update for Tesla API change - removal of api/1/powerwalls.
 * 05-Apr-2022 >>> v0.3.71.20220405 - Correct patch refresh date check integer overflow issue.
 * 05-Apr-2022 >>> v0.3.70.20220405 - Apparent Tesla auth change - quick patch.
 * 15-Mar-2022 >>> v0.3.61.20220315 - Added contact sensor capability to PW device to indicate grid status (open=off-grid).
 * 02-Feb-2022 >>> v0.3.60.20220202 - Add Storm Watch Active. Child device option for enhanced SmartThings/Hubitat integration.
 *                                    Delta threshold preference setting for power reporting. Enabled dimmer level for reserve control/status.
 * 30-Jan-2022 >>> v0.3.51.20220130 - Correct update delta check.
 * 28-Jan-2022 >>> v0.3.50.20220128 - Gateway debug and ping test.
 * 19-Jan-2022 >>> v0.3.41.20220119 - Cleanup. Ensure refresh token is always scheduled and old SmartThings schedules are cleared.
 * 18-Jan-2022 >>> v0.3.40.20220118 - Add option to choose between multiple powerwall sites. Fix on-grid actions.
 * 29-Dec-2021 >>> v0.3.30.20211229 - Merge and update of changes from @x10send: Added support for going off grid via local gateway (Hubitat Only). 
 *                                    Added ability to specify refresh token in lieu of access token.
 * 24-Oct-2021 >>> v0.3.20.20211024 - UI updates. Added Token expiration notification. Fixes: False off-grid notifications, 
 *                                    multiple SmartThings schedules, Gateway dashboard settings on Hubitat.
 * 02-Jun-2021 >>> v0.3.1e.20210603 - Re-add local gateway connection for Hubitat, Scheduling infrastructure mods. 
 * 25-May-2021 >>> v0.3.0e.20210325 - Tesla auth API change workarounds: use tokens directly, disable gateway direct code. 
 * 02-Jul-2020 >>> v0.2.8e.20200702 - Added dashboard tile display from local gateway iFrame for Hubitat. 
 * 27-May-2020 >>> v0.2.7e.20200527 - Handle extra null battery site info from Tesla. Handle no time zone set. 
 * 02-Mar-2020 >>> v0.2.6e.20200302 - Correct mobile notifications
 * 29-Feb-2020 >>> v0.2.5e.20200229 - Additional http command and query error checks. Added option to pause automations.
 * 19-Feb-2020 >>> v0.2.4e.20200219 - Added battery charge % trigger time and day restriction options.
 * 31-Jan-2020 >>> v0.2.3e.20200131 - Added battery charge % triggers & TBC Strategy scheduling.
 * 22-Jan-2020 >>> v0.2.2e.20200122 - Added Stormwatch on/off scheduling.
 * 16-Jan-2020 >>> v0.2.1e.20200116 - Additional command retry/error checking logic. Hubitat battery% compatibility update.
 * 10-Jan-2020 >>> v0.2.0e.20200110 - Push notification support for Hubitat
 * 04-Jan-2020 >>> v0.1.8e.20200104 - Updated async http call for cross-platform support with Hubitat & SmartThings
 * 03-Jan-2020 >>> v0.1.7e.20200103 - Added access token refresh & command post retry logic 
 * 30-Dec-2019 >>> v0.1.6e.20191230 - Increased reserve percentage value options 
 * 06-Sep-2019 >>> v0.1.5e.20190906 - Updated watchdog to only notify once when issue first occurs and when resolved 
 * 13-Aug-2019 >>> v0.1.4e.20190813 - Added grid/outage status display, notifications, and device on/off controls 
 * 09-Aug-2019 >>> v0.1.3e.20190809 - Added reserve% scheduling & polling interval preferences
 * 29-Jul-2019 >>> v0.1.2e.20190729 - Set reserve percent to 100% in backup-only mode. Added mode scheduling.
 * 23-Jul-2019 >>> v0.1.1e.20190723 - Initial beta release
 */

import groovy.transform.Field

definition (
    name: "Tesla Powerwall Manager - Tessie", namespace: "alan_f", author: "alan_f", description: "Monitor and control your Tesla Powerwall",
    importUrl: "https://raw.githubusercontent.com/youzer-name/Tesla-Powerwall-Manager---Tessie/master/smartapps/darwinsden/tesla-powerwall-manager.src/tesla-powerwall-manager.groovy",
    category: "My Apps",
    iconUrl: pwLogo,
    iconX2Url: pwLogo
)

preferences {
    page(name: "pageMain")
    page(name: "pageConnectionMethod")
    page(name: "teslaAccountInfo")
    page(name: "gatewayAccountInfo")
    page(name: "pageDashboardTile")
    page(name: "pageTessie")
    page(name: "pageNotifications")
    page(name: "pageSchedules")
    page(name: "pageScheduleOptions")
    page(name: "pageScheduleWhen")
    page(name: "pageDeleteSchedule", nextPage: "pageMain")
    page(name: "pageTriggers")
    page(name: "pageTriggerOptions")
    page(name: "pagePwActions")
    page(name: "pagePwPreferences")
    page(name: "pageDevicesToControl")
    page(name: "triggerRestrictions")
    page(name: "pageTokenFromUrl")
}

private pageMain() {
    return dynamicPage(name: "pageMain", title: "", install: true, uninstall: true) { 
        section() {
            if (hubIsSt()) {
                  paragraph app.versionDetails(), title: "PowerWall Manager", required: false, image: pwLogo
            }  else {
                paragraph "<img src ='${pwLogo}' align='left' style = 'padding-right: 15px'>Powerwall Manager\n ${app.versionDetails()}"
           }               
        }
        String connectStr
        if (hubIsSt()) {
            connectStr = "A Tesla server connection is required for access and control of the Powerwall through SmartThings." 
        } else {
            connectStr = "You can connect to the Powerwall through the Tesla server, your local gateway, or both. " + 
                 "A Tesla server connection is required for commanding Powerwall state changes. " +
                 "A local gateway connection allows more frequent Powerwall status updates than when connecting through the Tesla server alone."
        }
       if (!state.lastServerCheckTime || now() - state.lastServerCheckTime > 300000){
           getTeslaServerStatus()
       }
       if (!state.lastGatewayCheckTime || now() - state.lastGatewayCheckTime > 300000){
           getLocalGwStatus()
       }
       state.gwPingResults = null
       section(connectStr) {
            hrefMenuPage ("teslaAccountInfo", "Tesla Server Token Information..", state.serverStatusStr, teslaIcon, null, connectedToTeslaServer() ? "complete" : null)
            if (!hubIsSt()) {
                hrefMenuPage ("gatewayAccountInfo", "Local Powerwall Gateway Connection..", state.gatewayStatusStr, gatewayIcon, null, connectedToGateway() ? "complete" : null)
            }
        }


        section("Preferences") {
            hrefMenuPage ("pageTessie", "Tessie preferences", "", notifyIcon, null)
            hrefMenuPage ("pageNotifications", "Notification preferences..", "", notifyIcon, null)
            state.scheduleCount = state.scheduleCount ?: 0
            hrefMenuPage ("pageSchedules", "Schedule Powerwall setting changes..", "(${state.scheduleCount} active schedules)", schedIcon, null, state.scheduleCount > 0 ? "complete" : null)
            //String status = state.triggerActionsActive ? "Actions are enabled" : "Perform actions based on Powerwall charge %.."
            hrefMenuPage ("pageTriggers", "Perform actions based on Powerwall charge %..", "", batteryIcon, null, state.triggerActionsActive ? "complete" : null)
            Boolean valid = devicesToOffDuringOutage?.size() || devicesToOnAfterOutage?.size()
            hrefMenuPage ("pageDevicesToControl", "Turn off devices when a grid outage occurs..", "", outageIcon, null, valid ? "complete" : null)
            if (!hubIsSt()) {
                hrefMenuPage ("pageDashboardTile", "Display a dashboard tile iFrame from the gateway..", "", dashIcon, null, gatewayTileAddress ? "complete" : null)
            }
            hrefMenuPage ("pagePwPreferences", "Powerwall Manager General Preferences..", "", cogIcon, null)
        }
        
        section() {
            String freeMsg = "This is free software. Donations are very much appreciated, but are not required or expected."
            if (hubIsSt()) {
                href(name: "Site", title: "For more information, questions, or to provide feedback, please visit: ${ddUrl}",
                  description: "Tap to open the Powerwall Manager web page on DarwinsDen.com",
                  required: false,
                  image: ddLogoSt,
                  url: ddUrl)
                href(name: "", title: "",
                  description: freeMsg,
                  required: false,
                  image: ppBtn,
                  url: "https://www.paypal.com/paypalme/darwinsden")
            } else {             
                String ddMsg = "For more information, questions, or to provide feedback, please visit: <a href='${ddUrl}'>${ddUrl}</a>"
                String ddDiv = "<div style='display:inline-block;margin-right: 20px'>" + "<a href='${ddUrl}'><img src='${ddLogoHubitat}' height='40'></a></div>"
                String ppDiv = "<div style='display:inline-block'>" + "<a href='https://www.paypal.com/paypalme/darwinsden'><img src='${ppBtn}'></a></div>" 
                paragraph "<div style='text-align:center'>" + freeMsg + " " + ddMsg + "</div>"
                paragraph "<div style='text-align:center'>" + ddDiv + ppDiv + "</div>" 
            }
        }
    }
}

def pageSchedules() {
    setSchedules()
    state.scheduleDeleted = false
    state.editingScheduleIndex = -1
    dynamicPage(name: "pageSchedules", title: "Powerwall Schedules", install: false, uninstall: false) {
        section("") {
            state.scheduleCount = 0
            if (state.scheduleList && state.scheduleList.size() > 0) {
               state.scheduleList.eachWithIndex {item, index ->
                   String actionsStr = getActionsString(schedVal(item,"Mode"), schedVal(item,"Reserve"),schedVal(item,"Stormwatch"), schedVal(item,"GridCharging"), null, schedVal(item,"GridStatus"))
                   String whenStr = getWhenString(schedVal(item,"Time"), schedVal(item,"Days"),schedVal(item,"Months"))
                   Boolean actionsOk = actionsValid(schedVal(item,"Mode"), schedVal(item,"Reserve"),schedVal(item,"Stormwatch"),schedVal(item,"GridCharging"), null,schedVal(item,"GridStatus"))
                   Boolean whenOk = scheduleValid(schedVal(item,"Time"), schedVal(item,"Days"))
                   Boolean disabled = schedVal(item,"Disable") == "true"
 
                   String msgStr 
                   String icon 
                   if (!actionsOk) {
                       msgStr = "Actions are required. Select to add.."
                       icon = schedIncomplIcon
                   } else if (!whenOk) {
                       msgStr = "Requires time and days to be set. Select to add.."
                       icon = schedIncomplIcon
                   } else {
                       msgStr = whenStr + "\n" + actionsStr
                       icon = schedOkIcon
                   }   
                   Boolean scheduleActive = actionsOk && whenOk && !disabled
                   if (scheduleActive) {
                       state.scheduleCount = state.scheduleCount + 1
                   }
                   hrefMenuPage ("pageScheduleOptions", schedNameFromIndex(index), msgStr, icon, [schedIndex: index], scheduleActive ? "complete" : null)
                }
            } else {
                  paragraph "There are no active schedules."
           }
            if (!hubIsSt()) {
                //Apparent bug in Hubitat - Can't set params in a second 'section' or it will send that instead of what's in first section - keep all in the same section
                paragraph "\n"
                hrefMenuPage ("pageScheduleOptions", "Create a new Powerwall scheduled action..", "", addIcon, [newSchedule: true], null)
            }
        }
        if (hubIsSt() && state.scheduleCount < maxSmartThingsSchedules) {
            section("") {
                hrefMenuPage ("pageScheduleOptions", "Create a new Powerwall scheduled action..", "", addIcon, [newSchedule: true], null)
            }
        }
    }
}

String schedNameFromIndex (Integer schedIndex) {
    String schedName = "Schedule ${schedIndex + 1}"
    Integer schedNum = state.scheduleList[schedIndex]
    if (settings["schedule${schedNum}Name"]) {
        schedName = schedName + ": ${settings["schedule${schedNum}Name"]}"
    }
    if (settings["schedule${schedNum}Disable"]) {
        schedName = schedName + " (Disabled)"
    }
    return schedName
}

void appButtonHandler(btn) {
   switch (btn) {
       case "deleteSchedule":
          deleteScheduleIndex(state.editingScheduleIndex)
          break
       case "gatewayPing":
          def pingData = hubitat.helper.NetworkUtils.ping("${gatewayAddress}")
          state.gwPingResults = pingData
          state.lastGwPingIp = "${gatewayAddress}"
          logger ("Gateway ping results: ${pingData}","debug") 
          break
       default:
          logger ("Unknown button type: ${btn}","warn")
          break
   }    
}

def pageScheduleOptions(params) {
    Integer schedIndex
    if (state.editingScheduleIndex == -1) {
        if (params.newSchedule) {
            addNewSchedule()
            schedIndex = state.scheduleList.size() - 1
        } else if (params.schedIndex != null) {
            schedIndex = params.schedIndex
        } else {
            logger ("Unexpected condition in pageScheduleOptions. params are: ${params}","warn")
            schedIndex = state.editingScheduleIndex
        }
    } else {
        schedIndex = state.editingScheduleIndex
    } 
    Integer schedNum = state.scheduleList[schedIndex]
    if (state.scheduleDeleted) {
        dynamicPage(name: "pageScheduleOptions", title: "", install: false, uninstall: false) { 
            section("") {
                paragraph "Schedule ${schedIndex + 1} has been deleted"
            }
        }
    } else {
        dynamicPage(name: "pageScheduleOptions", title: schedNameFromIndex(schedIndex), install: false, uninstall: false) { 
            state.editingScheduleIndex = schedIndex
            section("Select Powerwall actions to apply:") {
               String actionsString = getActionsString(schedVal(schedNum,"Mode"), schedVal(schedNum,"Reserve"),schedVal(schedNum,"Stormwatch"), schedVal(schedNum,"GridCharging"), null, schedVal(schedNum,"GridStatus"))
               Boolean complete = actionsValid(schedVal(schedNum,"Mode"), schedVal(schedNum,"Reserve"),schedVal(schedNum,"Stormwatch"),schedVal(schedNum,"GridCharging"), null, schedVal(item,"GridStatus"))
               href "pagePwActions", title: actionsString, state: complete ? "complete" : null, description : "",
                   params: [prefix: "schedule${schedNum}", title : "Select at least one Powerwall action to apply:"]
            }
            section("Select when to perform these actions:") {
                String whenString = getWhenString(schedVal(schedNum,"Time"), schedVal(schedNum,"Days"),schedVal(schedNum,"Months"))
                Boolean complete = scheduleValid(schedVal(schedNum,"Time"), schedVal(schedNum,"Days"))
                href "pageScheduleWhen", title: whenString, state: complete ? "complete" : null, description: "", params: [schedIndex: schedIndex]
                paragraph getNextScheduleTime(schedNum)
            }
            section("") {
                input "schedule${schedNum}Name", "text", required: false, title: "Name this schedule (optional)"
                input "schedule${schedNum}Disable", "bool", required: false, defaultValue: false, title: "Disable this schedule", submitOnChange: true
            }        
            section("") {
                if (hubIsSt()) {
                    href "pageDeleteSchedule", title: "Delete this schedule", image: trashIcon, description: ""
                } else {
                    String trash = "<img src='${trashIcon}' width='25' style='float: left; width: 25px; margin: 5px 8px 0 0;'>"
                    input name: "deleteSchedule", type: "button", title: trash + "Delete this schedule",submitOnChange: true
                }
            }
        }
    }
}

def pageScheduleWhen(params) {
    Integer schedIndex
    if (params.schedIndex != null) {
          schedIndex = params.schedIndex
    } else {
        schedIndex = state.editingScheduleIndex
        logger ("Unexpected params in pageScheduleWhen: ${params}","warn")
    }
    Integer schedNum = state.scheduleList[schedIndex]
    dynamicPage(name: "pageScheduleWhen", title: schedNameFromIndex(schedIndex), install: false, uninstall: false) { 
        section("Select when to perform these actions:") {
            input "schedule${schedNum}Time", "time", required: false, title: "At what time? (required)", submitOnChange: true
            input "schedule${schedNum}Days", "enum", required: false, title: "On which days? (required)", multiple: true, submitOnChange: true,
                options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
            input "schedule${schedNum}Months", "enum", title: "In which months? (optional - if no months are selected, the schedule will execute for all months)", 
                required: false, multiple: true, submitOnChange: true,
                options: ["January": "January", "February": "February", "March": "March", "April": "April", "May": "May", "June": "June", "July": "July",
                    "August": "August", "September": "September", "October": "October", "November": "November", "December": "December"]
            paragraph getNextScheduleTime(schedNum)
        }
    }
}

String schedVal (Integer schedNum, String param) {
    return settings["schedule${schedNum}${param}"]
}

void clearScheduleData (data) {
    logger ("Clearing schedNum data: ${data.schedNum}","debug")
    Integer schedNum = data.schedNum
    app.updateSetting("schedule${schedNum}Name",[type:"text",value:""])
    app.updateSetting("schedule${schedNum}Mode",[type:"enum",value:""])
    app.updateSetting("schedule${schedNum}GridCharging",[type:"enum",value:""])
    app.updateSetting("schedule${schedNum}Months",[type:"enum",value:""])
    app.updateSetting("schedule${schedNum}Disable",[type:"bool",value:null])
    app.updateSetting("schedule${schedNum}Days",[type:"enum",value:""])
    app.updateSetting("schedule${schedNum}Stormwatch",[type:"enum",value:""])
    app.updateSetting("schedule${schedNum}GridStatus",[type:"enum",value:""])
    //app.updateSetting("schedule${schedNum}Time",[type:"text",value:""])
    app.removeSetting("schedule${schedNum}Time")
    app.updateSetting("schedule${schedNum}Reserve",[type:"enum",value:""])
}
    
void deleteScheduleIndex (Integer schedIndex) {
    Integer schedNum = state.scheduleList[schedIndex]
    logger ("Deleting schedule: ${schedIndex + 1}, number: ${schedNum}", "debug")
    state.scheduleNumUsed[schedNum-1] = false
    state.scheduleDeleted = true
    state.scheduleList.removeElement(schedNum)
    runIn(1, clearScheduleData, [data: [schedNum: schedNum]])
}
        
Integer addNewSchedule() {
    Integer schedNumAdded
    if (state.scheduleList == null) {
        state.scheduleNumUsed = []
        state.scheduleList = []
    }
    //Look for an unused schedule Number
    if (state.scheduleNumUsed.size() > 0) {
      for (int i in 0 .. state.scheduleNumUsed.size() - 1) {
        if (!state.scheduleNumUsed[i]) {
            logger ("Re-using schedule number ${i + 1}","debug")
            schedNumAdded = i + 1
            break
        }
      }
    }
    if (!schedNumAdded) {
        schedNumAdded = state.scheduleNumUsed.size() + 1
    }
    state.scheduleNumUsed[schedNumAdded - 1] = true
    logger ("Adding new schedule as with number: ${schedNumAdded}", "debug")
    state.scheduleList[state.scheduleList.size()] = schedNumAdded
}
  
def pageDeleteSchedule() {
	dynamicPage(name: "pageDeleteSchedule", title: "", nextPage: "pageMain", uninstall: false, install: false) {
			section() {
                    Integer schedIndex = state.editingScheduleIndex
       			    deleteScheduleIndex (schedIndex)
                	paragraph "Schedule ${schedIndex + 1} has been deleted."
			}
     }
}
    
String formatDate(Long unixTime) {
    def dateObject = new Date(unixTime)
    String time = ""
    if (unixTime) {
       def df = new java.text.SimpleDateFormat("EEE MMM dd yyyy HH:mm a z")
       // Ensure the new date object is set to local time zone
       if (location.timeZone != null) {
           df.setTimeZone(location.timeZone)
       } else {
           logger ("No time zone found for hub..","warn")
       }
       time = df.format(dateObject)
    }
    return "${time}"   
}

String getNextScheduleTime(Integer schedNum) {
    String nextExecTime = "(Next exec time: not scheduled)"
    try {
        Long currentTime = now()
        String nextTime = schedVal(schedNum,"Time")
        if (nextTime) {
            def nextTimeObject = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", nextTime)
            Long nextTimeEpoch = nextTimeObject.getTime();
            Long hr24 = 86_400_000
            while(nextTimeEpoch < currentTime + hr24 * 365) { 
               def date = new Date(nextTimeEpoch)
               if (nextTimeEpoch > currentTime && schedDayMonthValid(schedNum, getTheDay(date), getTheMonth(date))) {
                   if (hubIsSt()) {
                       nextExecTime = "Next exec time: ${formatDate(nextTimeEpoch)}"
                   } else {
                       nextExecTime = "(Next exec time: <span style='color:blue'>${formatDate(nextTimeEpoch)}</span>)"
                   }
                   break
               } else {
                   nextTimeEpoch = nextTimeEpoch + hr24
               }
            }
        }
    } catch (Exception e) {
        logger ("Exception getting next scheduled time for schedNum ${schedNum}: ${e}", "warn")
    }
    return nextExecTime            
}

String validatedAppender(Boolean valid) {
    String appender 
    if (valid) {
        if (hubIsSt()) {
            appender = " (validated)"
        } else {
            appender = "<span style='color : blue'> (validated)</span>"
        }
    } else {
        if (hubIsSt()) {
            appender = " (not validated)"
        } else {
            appender = "<span style='color : red'> (not validated)</span>"
        }
    }
}
    
private teslaAccountInfo() {
    state.serverVerified = false
    refreshAccessToken()
    getTeslaServerStatus()
    return dynamicPage(name: "teslaAccountInfo", title: "", install: false) {
        state.lastServerCheckTime = 0 // New data is being entered. Last server check is no longer valid
        if (hubIsSt() && accessTokenIp) {
            validateLocalUrl()
        } 
        
        String pString
        if (hubIsSt()) {
              pString = "This app currently requires a Tesla token generated using another app, such as the Tesla Auth App for " +
                   "IOS or Android, a web-based Tesla token generator, or from a script running on a local server. "
           } else {
               pString = "This app currently requires a Tesla token generated using another app, such as the Tesla Auth App for " +
                   "<a href='https://apps.apple.com/us/app/auth-app-for-tesla/id1552058613'>IOS</a> or " +
                   "Android, " +
                   "a web-based Tesla token generator, or from a <a href='https://github.com/enode-engineering/tesla-oauth2'>script</a> running on a local server. "
        }
        section ("Tesla Token Information") {
            paragraph pString
            input "inputRefreshToken", "text", title: "Refresh Token" + validatedAppender(state.refreshTokenSuccess), autoCorrect: false, required: false, submitOnChange : true
            if (state.siteSelector?.size() > 0 || settings.inputSite) {
                input(name: "inputSite", title: "Powerwall Site", type: "enum", required:false, multiple:false, options:state.siteSelector,submitOnChange : true)
            }
        }
        section(hideable: true, hidden: true, "OPTIONAL: You may alternatively supply an Access Token directly or serve one via a local server...") {
            
            paragraph ("If an Access Token is entered here, it must be updated periodically depending on the expiration date of the token provided (nominally every 45 days). " +
                       "If a valid Refresh Token is entered above, the Access Token will be periodically overridden.") 
            input "inputAccessToken", "text", title: "Access Token" + validatedAppender(validateInputToken()), autoCorrect: false, required: false, submitOnChange : true
            paragraph "You may configure a local server to generate and serve an Access Token. " + 
                "If local server information is provided below, this app will query the local server as needed " +
                "for updated access token information. The token must be" +
                " provided by your server in a JSON 'access_token' attribute, eg {'access_token' : 'xxxxxx'}."
            if (hubIsSt()) {
               String tokenFromUrlStatus 
               if (state.accessTokenFromUrlStatus && accessTokenIp) {
                  tokenFromUrlStatus = "Current Status: " + state.accessTokenFromUrlStatus 
               } else {
                  tokenFromUrlStatus = "Select to enter local server address"
               }
               href "pageTokenFromUrl", title: "Enter local URL information..", description: tokenFromUrlStatus, required: false
           } else {
                String tokenFromUrlStatus = ""
                if (accessTokenUrl) {
                    tokenFromUrlStatus = " - " + state.accessTokenFromUrlStatus
                }
                input "accessTokenUrl", "text", title: "URL on your local server to obtain access token (eg: http://192.168.1.100/tesla.html) ${tokenFromUrlStatus}", submitOnChange : true, autoCorrect: false, required: false
           }
        }
    }                        
}

def pageTokenFromUrl() {
    dynamicPage(name: "pageTokenFromUrl", title:"Get a token from a local URL.", install: false, uninstall: false) {
        section("") {
              state.useTokenFromUrl = false
              state.accessTokenFromUrlValid = false
              input "accessTokenIp", "text", title: "IP address of local server - eg: 192,168.1.30", autoCorrect: false, required: false
              input "accessTokenPath", "text", title: "Optional path on the local server - eg: /tesla/html", autoCorrect: false, required: false
        }
    }
}

private gatewayAccountInfo() {
    return dynamicPage(name: "gatewayAccountInfo", title: "", install: false) {
        state.lastGatewayCheckTime = 0 // New data is being entered. Last server check is no longer valid
        section("Local Gateway Information") {
            input "gatewayAddress", "text", title: "Powerwall Gateway IP local address (eg. 192.168.1.200)", required: false, submitOnChange: true 
            input "gatewayPw", "password", title: "Gateway Customer Password", autoCorrect: false, required: false, submitOnChange: true
        }
        section() {
            if (gatewayAddress) {
                paragraph "<span style='color:DarkGray'>${getLocalGwStatus()}</span>"
                input "gatewayPing", "button", title: "Test ping gateway", submitOnChange: true, width: 3
                if (state.gwPingResults && gatewayAddress==state.lastGwPingIp ) {
                    String result
                    if (state.gwPingResults.packetLoss) {
                        result = "<p style='margin-top:8px; color:red'>Issue pinging ${state.lastGwPingIp} from Hubitat.</p>"
                    } else {
                        result = "<p style='margin-top:8px; color:blue'>Hubitat successfully pinged ${state.lastGwPingIp}.</p>"
                    }
                    paragraph result, width: 9
                    paragraph "<p>${String.format('%tH:%<tM:%<tS', java.time.LocalDateTime.now())} ${state.gwPingResults}</p>"
                }
            }
        }
    }
}

String getConnectionMethodStatus() {
    String statusStr
    if (!connectionMethod) {
        statusStr = "Use Remote Tesla Account Server Only"
    } else {
        if (connectionMethod == "Use Local Gateway Only") {
            statusStr = connectionMethod.toString() + ".\n Note: A Tesla Server connection is required for full Powerwall Manager capabilities."
        } else {
           statusStr = connectionMethod.toString()
        }
    }
    return statusStr
}       

def pageConnectionMethod() {
    dynamicPage(name: "pageConnectionMethod", title:"Choose how to connect to the Powerwall.", install: false, uninstall: false) {
        section("Connection Method") {
             input "connectionMethod", "enum", required: false, defaultValue: "Use Remote Tesla Account Server Only", title: "Connection Method", 
                 options: ["Use Remote Tesla Account Server Only", "Use Local Gateway Only", "Use Both Tesla Server and Local Gateway"]
         }
    }
}

def pageDashboardTile() {
    dynamicPage(name: "pageDashboardTile", title:"Powerwall Dashboard iFrame Tile", install: false, uninstall: false) {
        String note = ""
        if (gatewayTileAddress) {
            section {
                createDashboardTile()
                paragraph "This tile can be displayed on a dashboard using the Tesla Powerwall Device with the custom attribute 'pWTile'"
                paragraph getTileStr(0.5)
            } 
        } else {
            note = "Enter address of gateway to create tile for dashboard:"
        }
        section(note) {
            input("gatewayTileAddress", "text", title: "Powerwall Gateway IP local address (eg. 192.168.1.200)", submitOnChange: true)
        }
        section {
            input("tileHeight", "number", title: "Height (default 517 pixels)", defaultValue: 517, submitOnChange: true, width: 4 )
            input("tileWidth", "number", title: "Width (default 460 pixels)", defaultValue: 460, submitOnChange: true, width: 4)
            input("tileScale", "decimal", title: "Scale (default 0.81)", defaultValue: 0.81, submitOnChange: true, width: 4)   
        }
        section{
            note = "To view this attribute tile on your dashboard, you may need to first visit the gateway URL in your dashboard browser, " +
                   "accept the self-signed certificate exception, and log in as 'customer'. Depending on your browser, you may be required to disable " +
                "'Prevent cross-site tracking' to view the gateway webpage frame." +
                   "\n&#8226Add to .css to remove extra tile padding in Fully Kiosk Browser: #tile-XXX .tile-contents {padding: 0; margin: -1px}"
            paragraph "<font style='font-size:14px; font-style: italic'>${note}</font>"    
        }
    }
}

Boolean connectedToGateway() {
    return state.gatewayVerified && gatewayAddress
}

Boolean connectedToTeslaServer() {
    Boolean connectedViaInputToken = inputAccessToken && state.inputAccessTokenValid
    Boolean connectedViaTokenFromUrl = state.accessTokenFromUrlValid && ((hubIsSt() && accessTokenIp) || (!hubIsSt() && accessTokenUrl))
    Boolean connectedViaTessie = useTessie //alan_f
    return state.serverVerified && (connectedViaInputToken || connectedViaTokenFromUrl || connectedViaTessie)      
}

String getTokenDateString() {
    if (inputAccessToken != state.lastInputAccessToken) {
        state.lastInputAccessToken = inputAccessToken
        state.tokenChangeTime = now()
        state.tokenAgeWarnSent = false
    }
    String msg = ""
    if (state.tokenChangeTime) {
        msg = "\nToken updated: ${formatDate(state.tokenChangeTime)}."                
        if (state.tokenExpiration) {
            msg = msg + "\nExpires: ${formatDate(state.tokenExpiration)}."
            if (!state.scheduleRefreshToken && state.refreshSchedTime && now() < state.refreshSchedTime) {
                msg = msg + "\nRefresh scheduled: ${formatDate(state.refreshSchedTime)}."
            } else if (state.scheduleRefreshToken) {
                msg = msg + "\nToken Refresh pending."
            } else if (state.tokenExpiration && now() > state.tokenExpiration) {
                msg = msg + "\nExpired."
            }
        }
    }
    return msg
}

void refreshAccessToken(){
    if (settings.inputRefreshToken && settings.inputRefreshToken != ""){
        String currentRefreshToken = settings.inputRefreshToken
        String ssoAccessToken = ""
        Long expiresIn
        state.refreshTokenSuccess = false
        Map payload = ["grant_type":teslaBearerTokenGrantType,"refresh_token":currentRefreshToken, "client_id":teslaBearerTokenClientId, "scope":teslaBearerTokenScope]
        try{
            logger ("Getting updated refresh token and bearer token for access token", "trace")
            logger ("Calling ${teslaBearerTokenEndpoint} with ${payload}","trace")
            httpPostJson([uri: teslaBearerTokenEndpoint, body: payload]){ resp ->
                Integer statusCode = resp.getStatus()
                if (statusCode == 200) {
                    logger("Bearer access request data: ${resp.data}","trace")
                    app.updateSetting("inputRefreshToken",[type:"text",value:resp.data["refresh_token"]])
                    ssoAccessToken = resp.data["access_token"]
                    expiresIn = resp.data.expires_in.toLong()
                    logger ("Successfully updated refresh token and bearer token for access token","debug")
                } 
                else {
                    logger ("Unable to update refresh token and bearer token for access token. Status code: ${statusCode}","warn")
                    if (now() < state.tokenExpiration) {
                        state.scheduleRefreshToken = true //Still time - try again later
                    }  
                }
            }
        }
        catch (Exception e){
            logger ("Error getting Tesla server bearer token from refresh token: ${e}","warn")
            if (now() < state.tokenExpiration) {
                state.scheduleRefreshToken = true //Still time - try again later
            }
        }
    
        logger ("Getting updated access token and expiry", "debug")
        Map ownerPayload = ["grant_type":teslaAccessTokenAuthGrantType, "client_id":teslaAccessTokenAuthClientId]
        Map ownerApiHeaders = ["Authorization": "Bearer " + ssoAccessToken]
        try{
            httpPostJson([uri: teslaAccessTokenEndpoint, headers: ownerApiHeaders, body: ownerPayload]){
                resp ->
                Integer statusCode = resp.getStatus()
                if (statusCode == 200){
                    logger("Access Token access request data: ${resp.data}","trace")
                    acceptAccessToken (resp.data["access_token"], resp.data.expires_in.toLong())
                }
                else {
                    logger ("Unable to update access token. Status code: ${statusCode}","debug")
                }
            }
        }
        catch (Exception e){
            logger ("Issue getting Tesla server access token from bearer refresh token: ${e}","debug")
            //Use the sso token as is:
            if (ssoAccessToken && expiresIn) {
                acceptAccessToken (ssoAccessToken, expiresIn)
            }
        }
    }
}

void acceptAccessToken (String token, Long expiresIn) {
    app.updateSetting("inputAccessToken",[type:"text",value:token])
    settings.inputAccessToken = token //ST workaround for immediate setting within dynamic page
    state.tokenExpiration = now() + expiresIn * 1000
    def refreshDate = new Date(state.tokenExpiration)
    logger ("Token expires on ${refreshDate}.","debug")
    state.scheduleRefreshToken = true  
    state.refreshTokenSuccess = true
    getTokenDateString() //Reset access token date status
}
    
String getTeslaServerStatus() {
    state.lastServerCheckTime = now()
    try {
        String messageStr = ""
        String tokenStatusStr = ""
        if (!hubIsSt() && accessTokenIp) {
            //Hubitat - local call is synchronous so can be done on this main page. For SmartThings
            //it is asynchronous, so needs to be done on the subpage and result will be available when on the main page
            validateLocalUrl()
         }
        state.useTokenFromUrl = state.accessTokenFromUrlValid
        state.useInputToken = validateInputToken()
        state.serverValidAtStartup = false
       
        Boolean tokenFromUrlEntered = (!hubIsSt() && accessTokenUrl) || (hubIsSt() && accessTokenIp)
        Boolean inputTokenEntered = inputAccessToken
        if (!inputTokenEntered && !tokenFromUrlEntered && !useTessie) { //alan_f
            messageStr = "You are not connected to the Tesla server.\nEnter your Tesla account token.."
            
        } else {
            if (state.useInputToken || state.useTokenFromUrl || useTessie) {
                getPowerwalls() 
                
                if (state.serverVerified) {
                    state.serverValidAtStartup = true
                    messageStr = messageStr + "You are connected to the Tesla server." +
                         "\nSite Name: ${state.siteName},  Id: ${state.pwId}." 
                    if (useTessie) { messageStr = messageStr + " Connected via Tessie." }
                    else { messageStr = messageStr + getTokenDateString() }
                } else {
                     messageStr = "Error: No Powerwalls found on Tesla server\n" +
                        "Please verify your Tesla Account access token."
                }
            } else {
               messageStr = messageStr + "Error Verifying Tesla/Powerwall Account\n" +
                   "Please verify your Tesla account access token."
            }
        }
        //Display token status if they are both entered, or if a token failed validation
        if (inputTokenEntered && (!state.useInputToken || tokenFromUrlEntered)) {
            messageStr = messageStr + "\nInput Access Token: ${state.inputAccessTokenStatus}. "
        }
        if (tokenFromUrlEntered && (!state.useTokenFromUrl || inputTokenEntered)) {
            messageStr = messageStr + "\nToken from URL: ${state.accessTokenFromUrlStatus}."
        }
         state.serverStatusStr = messageStr 
         return messageStr 
    } catch (Exception e) {
        logger ("Error getting Tesla server status: ${e}","warn")
        state.serverStatusStr = "Error accessing Powerwall account\n" + "Please verify your Tesla account access token. ${e}" 
        return state.serverStatusStr
    }
}                          

def gwHeader() {
    return ["Cookie" : "AuthCookie=${state.gwAuthCookie}; UserRecord=${state.gwUserRecord}"]
}

String getLocalGwStatus() {
    state.lastGatewayCheckTime = now()
    try {
        String messageStr
        state.gatewayVerified = false
        if (settings.gatewayAddress == null || settings.gatewayPw == null) {
            messageStr = "You are not connected to the local gateway.\nEnter your local gateway IP address and password.." 
        } else {
            logger ("Connecting to local gateway...","debug")
            messageStr = "Could not log in to local gateway at ${gatewayAddress}" 
            String gwUri = "https://${gatewayAddress}/api/login/Basic"
            logger("Posting to gateway URI: ${gwUri}","trace")
            httpPost([uri: gwUri,
                      contentType: 'application/json',
                      ignoreSSLIssues: true,
                      query: [username: "customer", password : "${gatewayPw}"]
                   ]) { resp ->
                Integer statusCode = resp.getStatus()
                logger("Gateway response status code: ${statusCode}","debug")
                if (statusCode == 200) {
                    resp.headers.each {
                        if (it.name == "Set-Cookie") {
                            String str = it.value
                            if (str.substring(0,10) == "UserRecord") {
                                state.gwUserRecord = str.substring(str.indexOf("=") + 1, str.indexOf(";"))
                            } else if (str.substring(0,10) == "AuthCookie") {
                                state.gwAuthCookie = str.substring(str.indexOf("=") + 1, str.indexOf(";"))
                            }
                        }
                    }
                    messageStr = "Could not verify local gateway auth cookie ${gatewayAddress}" 
                    httpGet([uri: "https://${gatewayAddress}", path: "/api/site_info/site_name", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true]) {
                        response -> 
                        logger("Local gateway connection verified","debug")
                        state.gatewayVerified = true
                        messageStr = "You are connected to the Powerwall Gateway.\n" +
                            //"Connected at ${gatewayAddress}\n"+
                            "Site Name: ${response.data.site_name.toString()}." 
                            //"Gateway time zone: ${response.data.timezone.toString()}\n"
                    }
                } else {
                    messageStr = "Unable to login to gateway at: ${gatewayAddress}. Status: ${statusCode}"
                }                  
            }
        }
        state.gatewayStatusStr = messageStr
        return messageStr
    } catch (Exception e) {
        logger ("Error getting local gateway status: ${e}","warn")
        state.gatewayStatusStr = "Error accessing local gateway at: ${gatewayAddress}.\n" + "Please verify your gateway address and password. ${e}" 
        return state.gatewayStatusStr
    }
}

    
def pageTessie() {
    dynamicPage(name: "pageTessie", title: "Tessie Preferences", install: false, uninstall: false) {
        section("Tessie Settings") {
            input "useTessie", "bool", required: false, defaultValue: false, title: "use Tessie API instead of Tesla API"
            input "tessieToken", "string", required: false, title: "Your Tessie API token"
	}
    }
}

def pageNotifications() {
    dynamicPage(name: "pageNotifications", title: "Notification Preferences", install: false, uninstall: false) {
        section("Notify me when..") {
            input "notifyWhenVersionChanges", "bool", required: false, defaultValue: false, title: "Powerwall software version changes"
            input "notifyWhenGridStatusChanges", "bool", required: false, defaultValue: false, title: "Grid status changes (power failures)"
            input "notifyWhenReserveApproached", "bool", required: false, defaultValue: false, title: "Powerwall charge level % drops to reserve percentage"
            input "notifyOfSchedules", "bool", required: false, defaultValue: false, title: "Schedules or charge % actions are being executed by the Powerwall Manager"
            input "notifyWhenModesChange", "bool", required: false, defaultValue: false, title: "Powerwall configuration (mode/schedule) changes are detected"
            input "notifyWhenAnomalies", "bool", required: false, defaultValue: true, title: "Anomalies are encountered in the Powerwall Manager"
            input "notifyWhenStormwatch", "bool", required: false, defaultValue: false, title: "Storm Watch mode is active"
            input "notifyOfTokenAge", "bool", required: false, defaultValue: true, title: "Access token has not been refreshed (40 days after entering)"
        }
        section() {
            if (hubIsSt()) {
                input "notificationMethod", "enum", required: false, defaultValue: "push", title: "Notification Method (push notifications are via mobile app)", options: ["none", "text", "push", "text and push"]
                input "phoneNumber", "phone", title: "Phone number for text messages", description: "Phone Number for text/SMS messages", required: false
            } else {
                //Hubitat
                input(name: "notifyDevices", type: "capability.notification", title: "Send to these notification devices", required: false, multiple: true, submitOnChange: true)
            }
        }
    }
}

def pagePwPreferences() {
    dynamicPage(name: "pagePwPreferences", title: "Powerwall Manager Preferences", install: false, uninstall: false) {
        section() {
            input "pollingPeriod", "enum", required: false, title: "Tesla server polling interval", defaultValue: "10 minutes (default)",
                options: ["Do not poll" : "Do not poll", "5 minutes" : "5 minutes", "10 minutes" : "10 minutes (default)", 
                          "30 minutes" : "30 minutes", "1 hour": "1 hour"]
            if (!hubIsSt()) {
                input "gatewayPollingPeriod", "enum", required: false, title: "Local gateway polling interval", defaultValue: "10 minutes (default)",
                   options: ["Do not poll" : "Do not poll", "1 minute" : "1 minute", "5 minutes" : "5 minutes", 
                             "10 minutes" : "10 minutes (default)", "30 minutes" : "30 minutes", "1 hour": "1 hour"]
            } 
            
            input "powerThreshold", "enum", required: false, 
                title: "Power change threshold required for power report updates. (Values will always be updated if changed by more than 50%.)", 
                defaultValue: "100 Watts (default)", 
                options: ["10" : "10 Watts", "50" : "50 Watts", "100" : "100 Watts (default)", "500" : "500 Watts", "1000": "1000 Watts"]
            
            input "logLevel", "enum", required: false, title: "Log level", defaultValue: "Info (default)", 
                options: ["none" : "No logging", "trace" : "Trace", "debug" : "Debug", "info" : "Info (default)", "warn" : "Warn", "error" : "Error"]
            label title: "Rename this app:", required: false
        }
        

        section () {
            paragraph "OPTIONAL: Child devices can be created in the Powerwall Manager Powerwall device preference settings (Hubitat Devices tab), providing " +
                "additional options for control and monitoring of Powerwall states and power levels via ${getHubType()}."
        }
        section (hideable: true, hidden: true, "Additional ${getHubType()} integration information...") {
            String builtIn
            String addtl
            if (hubIsSt()) {
                builtIn = "'Automations'"
                addtl = "Node-Red, WebCore, Sharp Tool"
            } else {
                builtIn = "'Simple Automation Rules'"
                addtl = "Node-Red, WebCore, Rule Machine, Sharp Tools"
            }
            paragraph "1) Enabling child devices in the Powerwall Manager Powerwall device preference settings allows for " +
                "control and monitoring of Powerwall states and power levels " +
                "via built-in ${getHubType()} apps such as ${builtIn}. Otherwise " +
                "rule engines (${addtl}, etc) that support custom commands and attributes are " +
                "required for extended integration of the Powerwall Manager with ${getHubType()}.\n"
            paragraph "2) The on/off switch and open/closed contact state of the Powerwall device can be used to indicate whether a grid outage has occured (on/closed=on-grid, off/open=grid-outage)." 
            paragraph "3) The 'dimmer' level of the Powerwall device can be used to command and monitor the Powerwall reserve (0-100%)."
            paragraph "4) The battery level of the Powerwall device will reflect the Powerwall charge level (0-100%)." 
        }       
    }
}

def pageDevicesToControl() {
    dynamicPage(name: "pageDevicesToControl", title: "Control devices in the event of a grid outage", install: false, uninstall: false) {
        section("") {
            input "devicesToOffDuringOutage", "capability.switch", title: "Devices to turn off during a grid outage", required: false, multiple: true
            input "turnDevicesBackOnAfterOutage", "bool", required: false, defaultValue: false,
                title: "Turn the above selected devices back On after grid outage is over? (Note: If set, the devices will be turned On regardless of their state prior to the outage)"
            input "devicesToOnAfterOutage", "capability.switch", title: "Devices to turn on when the grid outage is over", required: false, multiple: true
        }
    }
}

String appendOnNewLine(message, textToAdd) {
    if (textToAdd) {
       if (message) {
           message = message + "\n" + textToAdd
       } else {
           message = textToAdd
       }
    }
    return message
}

def pageTriggers() {
    dynamicPage(name: "pageTriggers", title: "Powerwall battery charge % level above/below actions.", install: false, uninstall: false) {
        //state.timeOfLastBelowTrigger = null
        //state.timeOfLastAboveTrigger = null

        section ("") {
            String message = ""
            Boolean actionsOk 
            state.triggerActionsActive = false
            //Above Actions
            actionsOk = actionsValid(aboveTriggerMode, aboveTriggerReserve, aboveTriggerStormwatch, aboveTriggerGridCharging, aboveTriggerDevicesToOn, aboveTriggerGridStatus) &&
                aboveTriggerValue && aboveTriggerEnabled?.toBoolean()
            state.triggerActionsActive = actionsOk
            if (actionsOk) {
                String actionsString = getActionsString(aboveTriggerMode, aboveTriggerReserve, aboveTriggerStormwatch, aboveTriggerGridCharging, aboveTriggerDevicesToOn, aboveTriggerGridStatus)
                message = "When Powerwall is above ${aboveTriggerValue?.toString()}%:\n" + actionsString + "\n(notification will also be sent if enabled in preferences)"
            } else {
                message = "Select to enable Upper % charge level actions.."
            }
            href "pageTriggerOptions", title: "Choose actions to execute when the Powerwall battery charge % rises above a pre-defined level:", state : actionsOk ? "complete" : null, 
                description: message, params : [aboveOrBelow : "above"]
 
            //Below Actions
            actionsOk = actionsValid(belowTriggerMode, belowTriggerReserve, belowTriggerStormwatch, belowTriggerGridCharging, belowTriggerDevicesToOff, belowTriggerGridStatus) &&  
                belowTriggerValue && belowTriggerEnabled?.toBoolean()
            state.triggerActionsActive = state.triggerActionsActive || actionsOk
            if (actionsOk) {
                String actionsString = getActionsString(belowTriggerMode, belowTriggerReserve, belowTriggerStormwatch, belowTriggerGridCharging, belowTriggerDevicesToOff, belowTriggerGridStatus)
                message = "When Powerwall is below ${belowTriggerValue?.toString()}%:\n" + actionsString + "\n(notification will also be sent if enabled in preferences)"
            } else {
                message = "Select to enable Lower % charge level actions.."
            }
            href "pageTriggerOptions", title: "Choose actions to execute when the Powerwall battery charge % drops below a pre-defined level:", state : actionsOk ? "complete" : null, 
                description: message, params : [aboveOrBelow : "below"]
            //Restrict Options
            String restrictMessage = ''
            Boolean restrictionSet = true
            if (triggerRestrictPeriod1?.toBoolean() && triggerStartTime1 && triggerStopTime1) {
                restrictMessage = appendOnNewLine(restrictMessage, "Trigger Period 1: " + formatTimeString(triggerStartTime1) + " to " + formatTimeString(triggerStopTime1))
            }
            if (triggerRestrictPeriod2?.toBoolean() && triggerStartTime2 && triggerStopTime2) {
                restrictMessage = appendOnNewLine(restrictMessage, "Trigger Period 2: " + formatTimeString(triggerStartTime2) + " to " + formatTimeString(triggerStopTime2))
            }
            if (triggerRestrictDays?.toBoolean() && triggerDays?.size() > 0) {
                restrictMessage = appendOnNewLine(restrictMessage, triggerDays.toString())
            }
            if (restrictMessage == '') {
                restrictMessage = "No optional schedule restrictions defined.."
                restrictionSet = false
            }
            href "triggerRestrictions", title: "Restrict these triggers to specific times/days (optional):", state : restrictionSet ? "complete" : null, description: restrictMessage
        }
    }
}

def triggerRestrictions() {
    dynamicPage(name: "triggerRestrictions", title: "Battery Charge % Level Trigger period restrictions", install: false, uninstall: false) {
        section("") {
            input "triggerRestrictDays", "bool", required: false, defaultValue: false, title: "Restrict % battery trigger actions to only occur on specified days"
            input "triggerDays", "enum", required: false, title: "Only on these days...", multiple: true,
                options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]

        }
        section("Restrict % battery trigger actions to only occur during specified time periods:") {
            input "triggerRestrictPeriod1", "bool", required: false, defaultValue: false, title: "Enable Time Period 1"
            input "triggerStartTime1", "time", required: false, title: "Time Period 1 Start Time"
            input "triggerStopTime1", "time", required: false, title: "Time Period 1 End Time"
            input "triggerRestrictPeriod2", "bool", required: false, defaultValue: false, title: "Enable Time Period 2"
            input "triggerStartTime2", "time", required: false, title: "Time Period 2 Start Time"
            input "triggerStopTime2", "time", required: false, title: "Time Period 2 Stop Time"
        }
    }
}

def pagePwActions(params) {
    String prefix = params.prefix
    String title = params.title
    if (params.prefix == null) {
        logger ("Unexpected param for PW action: ${params}","warn")
        prefix = state.lastPwActionPrefix
    }
    state.lastPwActionPrefix = prefix
    dynamicPage(name: "pagePwActions", title: title, install: false, uninstall: false) { 
        section() {
            input "${prefix}Mode", "enum", submitOnChange: true, required: false, title: "Set Mode", options: ["No Action", "Self-Powered", "Time-Based Control", "Backup-Only"]
            if (settings["${prefix}Mode"] == "Backup-Only") {
                paragraph "<span style='color:red'>Warning:</span> As of September 2021, Backup-Only has been deprecated by Tesla. Tesla recommends setting to Self-Powered " +
                    "with a Reserve of 100% instead." 
            }            
            input "${prefix}Reserve", "enum", required: false, title: "Set Reserve %",
               options: ["No Action": "No Action", "0": "0%", "5": "5%", "10": "10%", "15": "15%", "20": "20%", "25": "25%", "30": "30%", "35":
                      "35%", "40": "40%", "45": "45%", "50": "50%",
                      "55": "55%", "60": "60%", "65": "65%", "70": "70%", "75": "75%", "80": "80%", "85": "85%", "90": "90%", "95": "95%", "100": "100%"]
            input "${prefix}Stormwatch", "enum", required: false, title: "Set Storm Watch mode enable/disable", options: ["No Action", "Enable Stormwatch", "Disable Stormwatch"]
            input "${prefix}GridCharging", "enum", required: false, title: "Set Grid Charging enable/disable", options: ["No Action", "Enable Grid Charging","Disable Grid Charging"]
            if (!hubIsSt()){
                input "${prefix}GridStatus", "enum", required: false, title: "Set Grid Status", options: ["No Action", "Go On Grid","Go Off Grid"]
            }
        }
    }
}

def pageTriggerOptions(params) {
    String aboveBelow = params ? params.aboveOrBelow : state.aboveOrBelow
    state.aboveOrBelow = aboveBelow
    dynamicPage(name: "pageTriggerOptions", title: "Select '${aboveBelow}' Powerwall Charge % Level Trigger Options", install: false, uninstall: false) {
        section("") {
            input "${aboveBelow}TriggerEnabled", "bool", required: false, defaultValue: false, title: "Enable these actions"
            input "${aboveBelow}TriggerValue", "number", required: false, title: "Actions trigger when charge % is ${aboveBelow} this value:"
            String onOrOff = aboveBelow == "above" ? "On" : "Off"
            input "${aboveBelow}TriggerDevicesTo${onOrOff}", "capability.switch", title:
                "Select devices to turn ${onOrOff} when charge level % is ${aboveBelow} defined trigger", required: false, multiple: true
            Boolean complete = actionsValid(settings["${aboveBelow}TriggerMode"], settings["${aboveBelow}TriggerReserve"],settings["${aboveBelow}TriggerStormwatch"],settings["${aboveBelow}TriggerGridCharging"], null, settings["${aboveBelow}TriggerGridStatus"])
            String actionsString 
            if (complete) {
               actionsString = getActionsString(settings["${aboveBelow}TriggerMode"], settings["${aboveBelow}TriggerReserve"],settings["${aboveBelow}TriggerStormwatch"], 
                           settings["${aboveBelow}TriggerGridCharging"], null,settings["${aboveBelow}TriggerGridStatus"])
            } else {
               actionsString = "No Powerwall actions defined.."
            }
            href "pagePwActions", title: "Select Powerwall actions to apply when charge level % is ${aboveBelow} defined trigger", state: complete ? "complete" : null, description: actionsString, 
               params: [prefix: "${aboveBelow} Trigger", title : "Select ${aboveBelow} trigger Powerwall actions to apply:"]
       }
    }
}

Boolean hubIsSt() {
    return (getHubType() == "SmartThings")
}

def getPwDevice() {
   def deviceIdStr = null
   def device
   if (state.childDeviceId) {
      deviceIdStr = state.childDeviceId
      device = getChildDevice(deviceIdStr)
   }
   if (!device) {
      def devices = getChildDevices()
      if (devices.size() > 0) {
          deviceIdStr = getChildDevices().first().getDeviceNetworkId()
          state.childDeviceId = deviceIdStr
          device = getChildDevice(deviceIdStr)
      }
   } 
   return device
}

private getHubType() {
    String hubType = "SmartThings"
    if (state.hubType == null) {
        try {
            include 'asynchttp_v1'
        } catch (e) {
            hubType = "Hubitat"
        }
        state.hubType = hubType
    }
    return state.hubType
}

Boolean actionsValid(modeSetting, reserveSetting, stormwatchSetting, gridChargingSetting, devicesToControl, gridStatus) {
    logger("Mode setting: ${modeSetting}, Reserve: ${reserveSetting}, Stormwatch: ${stormwatchSetting}, GridCharging: ${gridChargingSetting}, devicesToControl: ${devicesToControl}, Grid Status: ${gridStatus}","debug")
    return ((modeSetting && modeSetting.toString() != "No Action") ||
        (reserveSetting && reserveSetting.toString() != "No Action") ||
        (stormwatchSetting && stormwatchSetting.toString() != "No Action") ||
        (gridChargingSetting && gridChargingSetting.toString() != "No Action") ||
        (gridStatus && gridStatus.toString() != "No Action") ||
        (devicesToControl && devicesToControl.toString() != "N/A" && devicesToControl.size() > 0))
}

Boolean scheduleValid(timeSetting, daysSetting) {
    return timeSetting != null && daysSetting != null && (daysSetting.size() > 0 || daysSetting.toString() == "N/A")
}

String formatTimeString(timeSetting) {
    def timeFormat = new java.text.SimpleDateFormat("hh:mm a")
    String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    def isoTime = new java.text.SimpleDateFormat(isoDatePattern).parse(timeSetting.toString())
    return timeFormat.format(isoTime).toString()
}

String getWhenString(timeSetting, daysSetting, monthSetting) {
    String str
    if (scheduleValid(timeSetting, daysSetting)) {
        String timeString = ''
        if (timeSetting != "N/A") {
            timeString = formatTimeString(timeSetting) + ' '
        }
        String dayString = ''
        if (daysSetting != "N/A") {
            dayString = daysSetting.toString()
        }
        if (timeString != '' || dayString != '') {
             str = timeString + dayString
        }
        if (monthSetting && monthSetting != '' & monthSetting != "N/A" ) {
            str = str + "\nMonths: " + monthSetting.toString()
        }
    } else {
        str = "Requires time and days to be set. Select to add.."
    }
    return str
}

String getActionsString(modeSetting, reserveSetting, stormwatchSetting, gridChargingSetting, controlDevices, gridStatusSetting) {
    String str = ''
    if (actionsValid(modeSetting, reserveSetting, stormwatchSetting, gridChargingSetting, controlDevices, gridStatusSetting)) {
        if (modeSetting && modeSetting.toString() != "No Action") {
            str = "Mode: " + modeSetting.toString()
        }
        if (reserveSetting && reserveSetting.toString() != "No Action") {
            str = appendOnNewLine(str, "Reserve: " + reserveSetting.toString() + '%')
        }
        if (stormwatchSetting && stormwatchSetting.toString() != "No Action") {
            if (stormwatchSetting.toString() == "Enable Stormwatch") {
                str = appendOnNewLine(str, "Stormwatch: Enable")
            } else if (stormwatchSetting.toString() == "Disable Stormwatch") {
                str = appendOnNewLine(str, "Stormwatch: Disable")
            }
        }
        if (gridChargingSetting && gridChargingSetting.toString() != "No Action") {
            if (gridChargingSetting.toString() == "Enable Grid Charging") {
                str = appendOnNewLine(str, "Grid Charging: Enable")
            } else if (gridChargingSetting.toString() == "Disable Grid Charging") {
                str = appendOnNewLine(str, "Grid Charging: Disable")
            }
        }        
        if (controlDevices && controlDevices.size() > 0) {
            str = appendOnNewLine(str, "Control Devices: ${controlDevices}")               
        }
        if (gridStatusSetting && gridStatusSetting.toString() != "No Action") {
            str = appendOnNewLine(str, "Grid Status: " + gridStatusSetting.toString())
        }
    } else {
        str = "At least one action is required. Select to add.."
    }
    return str
}
                                                              
void setSchedules() {
    if (hubIsSt()) {
        for(int i in 1 .. maxSmartThingsSchedules) {
            unschedule("processSchedule${i}")
        }
    } else {
        unschedule ("processSchedule") //possible minor Hubitat bug - pass method as string otherwise will unschedule everything if method does not exist
    }
    if (state.scheduleList) {
       for(int i in 0 .. state.scheduleList.size() - 1) {
           Integer schedNum = state.scheduleList[i]
           if (!(schedVal(schedNum,"Disable") == "true")) {
             if (actionsValid(schedVal(schedNum,"Mode"), schedVal(schedNum,"Reserve"), schedVal(schedNum,"Stormwatch"), schedVal(schedNum,"GridCharging"), null, schedVal(schedNum,"GridStatus"))) {
                if (scheduleValid(schedVal(schedNum,"Time"), schedVal(schedNum,"Days"))) {
                    logger ("Scheduling index: ${i + 1} num: ${schedNum} for time ${schedVal(schedNum,"Time")}","debug")
                    if (hubIsSt()) {
                        schedule(schedVal(schedNum,"Time"), "processSchedule${schedNum}", [data: [schedNum: schedNum]])  //overwite is not working on ST
                    } else {
                        schedule(schedVal(schedNum,"Time"), processSchedule, [data: [schedNum: schedNum], overwrite: false])  
                    }
                    //schedule(schedVal(schedNum,"Time"), processSchedule, [data: [schedNum: schedNum], overwrite: false])  //[data: [message: msg], overwrite: false]
                } else {
                    String msg = "Powerwall Manager Schedule index: ${i + 1} num: ${schedNum}. Actions are enabled in preferences, but schedule time and/or days were not specified. Schedule could not be set."
                    logger (msg,"warn")
                    //sendNotificationMessage(msg, "anomaly")
                }
              }
          } else {
               logger ("Schedule index: ${i + 1} num ${schedNum} is disabled","debug")
         }
       }
    }
}

String getTheDay(date=null) {
    if (!date) {
        date = new Date()
    }
    def df = new java.text.SimpleDateFormat("EEEE")
    // Ensure the new date object is set to local time zone
    if (location.timeZone != null) {
        df.setTimeZone(location.timeZone)
    } else {
         logger ("no time zone found for hub - schedule processing day","warn")
    }
    return df.format(date)
}

String getTheMonth(date=null) {
    if (!date) {
        date = new Date()
    }
    def mf = new java.text.SimpleDateFormat("MMMM")
    if (location.timeZone != null) {
        mf.setTimeZone(location.timeZone)
    } else {
        logger ("no time zone found for hub - schedule processing month","warn")
    }
    return mf.format(date)
}

//Hubitat compatibility
private timeOfDayIsBetween(fromDate, toDate, checkDate, timeZone) {
    return (!checkDate.before(toDateTime(fromDate)) && !checkDate.after(toDateTime(toDate)))
}

Boolean triggerPeriodActive() {
    String day = getTheDay()
    Boolean daysAreSet = triggerRestrictDays?.toBoolean() && triggerDays?.size() > 0
    Boolean dayIsActive = daysAreSet && triggerDays?.contains(day)
    Boolean aPeriodIsSet = (triggerRestrictPeriod1?.toBoolean() || triggerRestrictPeriod2?.toBoolean())
    Boolean aPeriodIsActive = (triggerRestrictPeriod1?.toBoolean() && timeOfDayIsBetween(triggerStartTime1, triggerStopTime1, new Date(), location.timeZone)) ||
        (triggerRestrictPeriod2?.toBoolean() && timeOfDayIsBetween(triggerStartTime2, triggerStopTime2, new Date(), location.timeZone))
    //Valid conditions:
    // 1) day matches & period active, 2) day matches & no periods declared, 3) no day is set & period active, 4) no day is set & no periods declared 
    return ((dayIsActive && (aPeriodIsActive || !aPeriodIsSet)) || (!daysAreSet && (aPeriodIsActive || !aPeriodIsSet)))
}

String commandPwActions(mode, reserve, stormwatch, gridCharging, enableChargeTriggers, gridStatus) {
    def pwDevice = getPwDevice()
    String message = ""
    if (mode && mode.toString() != "No Action") {
        message = message + " Mode: ${mode.toString()}."
        if (mode.toString() == "Backup-Only") {
            setBackupOnlyMode(pwDevice)
            //runIn(2, commandBackupReservePercent, [data: [reservePercent: 100]])
            //String errMessage = "Backup-Only mode no longer supported by Powerwall. Setting reserve to 100%"
            //sendNotificationMessage(errMessage, "anomaly")
        } else if (mode.toString() == "Self-Powered") {
            setSelfPoweredMode(pwDevice)
        } else if (mode.toString() == "Time-Based Control") {
            setTimeBasedControlMode(pwDevice)
        } else {
            String errMessage = "Unexpected condition processing scheduled mode change: ${mode.toString()}"
            sendNotificationMessage(errMessage, "anomaly")
        }
    }
    if (reserve && reserve.toString() != "No Action") {
        message = message + " Reserve: ${reserve}%."
        if (reserve.toInteger() >= 0 && reserve.toInteger() <= 100) {
            runIn(10, commandBackupReservePercent, [data: [reservePercent: reserve.toInteger()]])
        } else {
            String errMessage = "Unexpected condition processing scheduled reserve % change: ${reserve}}"
            sendNotificationMessage(errMessage, "anomaly")
        }
    }
    if (stormwatch && stormwatch.toString() != "No Action") {
        if (stormwatch.toString() == "Enable Stormwatch") {
            runIn(15, commandStormwatchEnable)
            message = message + " Stormwatch: Enabled."
        } else if (stormwatch.toString() == "Disable Stormwatch") {
            message = message + " Stormwatch: Disabled."
            runIn(15, commandStormwatchDisable)
        }
    }
    if (gridCharging && gridCharging.toString() != "No Action") {
        message = message + " Grid Charging: ${gridCharging.toString()}."
        if (gridCharging.toString() == "Enable Grid Charging") {
            runIn(20, commandGridChargingEnable)
        } else if (gridCharging.toString() == "Disable Grid Charging") {
            runIn(20, commandGridChargingDisable)
        } else {
            String errMessage = "Unexpected condition processing scheduled gridCharging change: ${gridCharging.toString()}"
            sendNotificationMessage(errMessage, "anomaly")
        }
    }
    if (enableChargeTriggers && enableChargeTriggers.toString() != "No Action") {
        if (enableChargeTriggers.toString() == "Turn On Peak") {
            message = message + " Virtual Peak Switch: On."
        } else if (stormwatch.toString() == "Disable Triggers") {
            message = message + " Virtual Peak Switch: Off."
        }
    }
    
    if (gridStatus && gridStatus.toString() != "No Action") {
        if (gridStatus.toString() == "Go On Grid") {
            runIn(2, commandGoOffGrid, [data: [isOnGrid:true]])
            message = message + " Going On Grid."
        } else if (gridStatus.toString() == "Go Off Grid"){
            runIn(2, commandGoOffGrid, [data: [isOnGrid:false]])
            message = message + " Going Off Grid."
        }
    }
    return message
}

Boolean schedDayMonthValid (Integer schedNum, String day, String month) {
    Boolean monthValid = !schedVal(schedNum,"Months") || schedVal(schedNum,"Months").contains(month)
    Boolean dayValid = schedVal(schedNum,"Days") && schedVal(schedNum,"Days").contains(day)
    return dayValid && monthValid && !(schedVal(schedNum,"Disable") == "true")
}

void processSchedule(data) {
    Integer schedNum = data.schedNum
    if (schedDayMonthValid(schedNum, getTheDay(), getTheMonth())) {
        logger ("Executing schedule number ${schedNum}","debug")
        String message = commandPwActions(schedVal(schedNum,"Mode"), schedVal(schedNum,"Reserve"), schedVal(schedNum,"Stormwatch"), schedVal(schedNum,"GridCharging"), null, schedVal(schedNum,"GridStatus"))
        if (notifyOfSchedules?.toBoolean()) {
            sendNotificationMessage("Performing scheduled Powerwall actions. " + message)
        }
    } else {
        logger ("Schedule number: ${schedNum} not executing due to day/month/disable criteria", "debug")
    }
}

// SmartThings requires explicit schedule declarations since schedule: overwite appears to not work. This currently limits the schedule count in ST. 
void processSchedule1(data) {
    processSchedule (data)
}
void processSchedule2(data) {
    processSchedule (data)
}
void processSchedule3(data) {
    processSchedule (data)
}
void processSchedule4(data) {
    processSchedule (data)
}
void processSchedule5(data) {
    processSchedule (data)
}
void processSchedule6(data) {
    processSchedule (data)
}
void processSchedule7(data) {
    processSchedule (data)
}
void processSchedule8(data) {
    processSchedule (data)
}
void processSchedule9(data) {
    processSchedule (data)
}
void processSchedule10(data) {
    processSchedule (data)
}
void processSchedule11(data) {
    processSchedule (data)
}
void processSchedule12(data) {
    processSchedule (data)
}
void processSchedule13(data) {
    processSchedule (data)
}
void processSchedule14(data) {
    processSchedule (data)
}
void processSchedule15(data) {
    processSchedule (data)
}

private getId() {
    "81527cff06843c8634fdc09e8ac0abefb46ac849f38fe1e431c2ef2106796384"
}
private getSecret() {
    "c7257eb71a564034f9419ee651c7d0e5f7aa6bfbd18bafb5c5c033b093bb2fa3"
}
private getAgent() {
    "TeslaApp/4.10.0"
    //"darwinsden/hubitat"
}
    
String getToken() {
    String returnToken = null
    if (state.useInputToken) {
        returnToken = inputAccessToken
    } else if (state.useTokenFromUrl) {
        returnToken = state.accessTokenFromUrl
    }
    return returnToken
}

private httpAsyncGet (handlerMethod, String url, String path, query=null) {
    try {
        def requestParameters = [uri: url, path: path, query: query, contentType: 'application/json']
        if(hubIsSt()) {
            include 'asynchttp_v1'
            asynchttp_v1.get(handlerMethod, requestParameters)
        } else { 
            asynchttpGet(handlerMethod, requestParameters)
        }
    } 
    catch (e) {
       log.error "Http Get failed: ${e}"
    }
}

private httpAuthAsyncGet(handlerMethod, String path, Integer attempt = 1) {
    if (useTessie) {
      try {
          logger ("Async requesting: ${path}","trace")
          def requestParameters = [
              uri: tessieUrl,
              path: path,
              headers: ['User-Agent': agent, 'X-Tesla-User-Agent': agent, Authorization: "Bearer ${tessieToken}"]
            ]
          if (hubIsSt()) {
              include 'asynchttp_v1'
              asynchttp_v1.get(handlerMethod, requestParameters, [attempt: attempt])
          } else {
              asynchttpGet(handlerMethod, requestParameters, [attempt: attempt])
          }
      } catch (e) {
          log.error "Http Async Get failed: ${e}"
      }
    } 
    else
    {
    String theToken = getToken()
    if (theToken) {
      try {
          logger ("Async requesting: ${path}","trace")
          def requestParameters = [
              uri: teslaUrl,
              path: path,
              headers: ['User-Agent': agent, 'X-Tesla-User-Agent': agent, Authorization: "Bearer ${theToken}"]
            ]
          if (hubIsSt()) {
              include 'asynchttp_v1'
              asynchttp_v1.get(handlerMethod, requestParameters, [attempt: attempt])
          } else {
              asynchttpGet(handlerMethod, requestParameters, [attempt: attempt])
          }
      } catch (e) {
          log.error "Http Async Get failed: ${e}"
      }
    } else {
        logger("Async request to ${path} not sent. Token is invalid","warn")
    }
}
}

private httpAuthGet(String path, Closure closure, authToken = null) {
    def requestParameters = []//alan_f    
    //There is no exception handling here, so that the exception can be uniquely handled by the calling method. 
    if (authToken == null) {
        authToken = token
    }
    if (useTessie) {
        fulluri = tessieUrl + path
        requestParameters = [uri: fulluri, headers: [Authorization: "Bearer ${tessieToken}"]] 
        logger("request parameters = " + requestParameters, "debug")
    } //alan_f
    else
    {
    requestParameters = [uri: teslaUrl, path: path, headers: ['User-Agent': agent, 'X-Tesla-User-Agent': agent, Authorization: "Bearer ${authToken}"]]
    }
    
    httpGet(requestParameters) {resp -> closure(resp)}
    
}

private httpAuthPost(Map params = [:], String cmdName, String path, Closure closure, Integer attempt = null) {
    //cmdName is descriptive name for logging/notification
    Integer tryCount = attempt ?: 1
    String attemptStr = ""
    if (tryCount > 1) {
        attemptStr = ", Attempt: ${tryCount}"
    }
    String authToken = getToken()
    if (authToken || useTessie) {  //alan_f
       logger ("Command: ${cmdName} ${params?.body}" + attemptStr,"debug")
       try {
           //alan_f
           def requestParameters
           if (useTessie) {
           requestParameters = [uri: tessieUrl, path: path, headers: ['User-Agent': agent, 'X-Tesla-User-Agent': agent, Authorization: "Bearer ${tessieToken}"]]
           }
           else
           {
           requestParameters = [uri: teslaUrl, path: path, headers: ['User-Agent': agent, 'X-Tesla-User-Agent': agent, Authorization: "Bearer ${authToken}"]]
           }
           if (params.body) {
               String jsonbody = new groovy.json.JsonOutput().toJson(params.body)
               requestParameters["body"] = jsonbody
               httpPostJson(requestParameters) {resp -> closure(resp)}
           } else {
               httpPost(requestParameters) {resp -> closure(resp)}
           }
           state.cmdFailedSent = false
       } catch (groovyx.net.http.HttpResponseException e) {
           if (tryCount < 3) {
               logger ("Request attempt ${tryCount} failed for path: ${path}. HTTP status code: ${e?.response?.getStatus()}","debug")
               if (e?.response?.getStatus() == 401) {
                   handleServerAuthIssue()
                   pause(2000)
               }
               pause(1000)
               httpAuthPost(params, cmdName, path, closure, tryCount + 1)
           } else {
               logger ("Request failed after ${tryCount} attempts for path: ${path}. HTTP status code: ${e?.response?.getStatus()}","warn")
               if (!state.cmdFailedSent) {
                   sendNotificationMessage("Powerwall Manager: Failed HTTP command: ${cmdName} after ${tryCount} tries.")
                   state.cmdFailedSent = true
               }
           }
       } catch (Exception e) {
           if (tryCount < 3) {
               logger ("Request attempt ${tryCount} failed for path: ${path}. General Exception: ${e}","debug")
               pause(1000)
               httpAuthPost(params, cmdName, path, closure, tryCount + 1)
           } else {
               logger ("Request failed after ${tryCount} attempts for path: ${path}. General Exception: ${e}","warn")
               if (!state.cmdFailedSent) {
                   sendNotificationMessage("Powerwall Manager: Failed command: ${cmdName} after ${tryCount} tries.")
                   state.cmdFailedSent = true
               }
           }
       }
    } else {
        logger ("Cannot send command: ${cmdName}. Token is not valid","warn")
    }
}

private sendNotificationMessage(message, msgType = null) {
    logger ("notification message: ${message}","debug")
    if (msgType == null || msgType != "anomaly" || notifyWhenAnomalies?.toBoolean()) {
        if (hubIsSt()) {
           Boolean sendPushMessage = (!notificationMethod || (notificationMethod.toString() == "push" || notificationMethod.toString() == "text and push"))
           Boolean sendTextMessage = (notificationMethod?.toString() == "text" || notificationMethod?.toString() == "text and push")
           if (sendTextMessage == true) {
               if (phoneNumber) {
                  sendSmsMessage(phoneNumber.toString(), message)
               }
           }
           if (sendPushMessage) {
               sendPush(message)
           }
        } else {
           // Hubitat
           if (notifyDevices != null) {
               notifyDevices.each {
                   it.deviceNotification(message)
               }
           }
      }
    }
}
    
private getPowerwalls() {
    state.serverVerified = false
    state.siteSelector = [:]
    Boolean foundPowerwall = false
    try {
       httpAuthGet("/api/1/products", {
        resp ->
        logger ("response data for products is ${resp.data}","trace")
        resp.data.response.each {
            product ->
                if (product.resource_type == "battery") {
                    state.siteSelector[product.energy_site_id] = "${product.energy_site_id} - ${product.id} - ${product.site_name}"
                    //do not consider battery site if its site_name is null and a battery has previously been found (possibly a bad second site in the database)
                    if (settings.inputSite.toString() == product.energy_site_id.toString() || (!settings.inputSite && (product.site_name != null || !foundPowerwall))) {
                        foundPowerwall = true
                        app.updateSetting("inputSite",[type:"enum",value:product.energy_site_id.toString()])
                        settings.inputSite = product.energy_site_id.toString() //ST workaround for immediate setting within dynamic page
                        logger ("battery found: ${product.id} site_name: ${product.site_name} energy_site_id: ${product.energy_site_id}","debug")
                        state.energySiteId = product.energy_site_id
                        state.pwId = product.id
                        state.siteName = product.site_name
                    }
                }
        }
    })
    } catch (Exception e) {
       log.error  "Exception checking for Powerwalls: ${e}"
    }
    state.serverVerified = foundPowerwall
}

def installed() {
    log.debug("${app.label} installed.")
    runIn (1, initialize)
}

def updated() {
    logger ("${app.label} updated","debug")
    initialize()
}

def uninstalled() {
    logger ("uninstalling","info")
    removeChildDevices(getChildDevices())
}

private removeChildDevices(delete) {
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}

void pollProcedure(String period, def procedure) {
    switch(period) {
        case "1 minute":
            runEvery1Minute(procedure)
            break
        case "5 minutes": 
            runEvery5Minutes(procedure)
            break
        case "10 minutes": 
            runEvery10Minutes(procedure)
            break
        case "30 minutes": 
            runEvery30Minutes(procedure)
            break
        case "1 hour": 
            runEvery1Hour(procedure)
            break
        case "Do not poll": 
            break
        default:
            runEvery10Minutes(procedure)
            break
       }
}

void startPollingServer() {
    pollProcedure(pollingPeriod, processServerMain)
}
    
void startPollingGateway() {
    pollProcedure(gatewayPollingPeriod, processGatewayMain)
}

def initialize() {
    unsubscribe()
    unschedule()
    state.lastHeartbeatUpdateTime = [:]
    createDeviceForPowerwall()

    setSchedules()
 
    schedule(new Date(), versionCheck)
    if (gatewayTileAddress) {
        runIn (10, createDashboardTile)
    }

    //stagger server and gateway polling
    runIn (30, startPollingServer)
    if (connectedToGateway()) {
       startPollingGateway()
    }
    
    runEvery3Hours(processWatchdog)
    //runEvery3Hours(refreshAccessToken)
    runIn(10, processServerMain)
    runIn(15, processGatewayMain)
    if (state.tokenExpiration) {
        state.scheduleRefreshToken = true
    }       
}

private createDeviceForPowerwall() {
    def pwDevice = getPwDevice()
    if (!pwDevice) {
        String dni = "Powerwall-" + app.id.toString()
        log.debug "creating Powerwall device dni: ${dni}"
        def device = addChildDevice("darwinsden", "Tesla Powerwall", dni, null, 
             [name: "Tesla Powerwall", label: "Powerwall", completedSetup: true ])
    } else {
        logger ("device for Powerwall exists","trace")
        pwDevice.initialize()
    }
}

void createDashboardTile() {
   def pwDevice = getPwDevice()
   logger ("creating/updating tile...","debug")
   if (pwDevice) {
       String tileStr = getTileStr(tileScale?.toFloat()) 
       pwDevice.sendEvent(name: "pwTile", value: tileStr)
   } else {
       logger("Unable to update Dashboard tile. Powerwall device does not exist.","warn")
   }
}

String versionDetails () {
    String vers = app.version() 
    if (newerVersionExists(state.latestStableVersion, app.version())) {
        String latestVersion
        if (!hubIsSt()) {
            latestVersion = "<a href='${ddUrl}'>${state.latestStableVersion}</a>"
        } else {
            latestVersion = state.latestStableVersion
        }
        vers = vers + " (${latestVersion} is available)"
    }
    return vers
}      
        
String stripVerPrefix(String ver) {
    if (ver && (ver.substring(0,1) == 'v' || ver.substring(0,1) == 'V')) {
        ver = ver.substring(1,ver.size() - 1)
    }
    return ver
}
       
Boolean newerVersionExists(latest, current) {
    Boolean isNewer = false
    if (latest && current) {
        List latV = stripVerPrefix(latest).tokenize('.')
        List curV = stripVerPrefix(current).tokenize('.')
        if (latV.size() >= 3 && curV.size() >= 3) {
            isNewer = !(curV[0] >= latV[0] && curV[1] >= latV[1] && curV[2] >= latV[2]) 
        }
    }
    return isNewer
}
    
void versionCb (resp, callData) {
    if (resp.status == 200) {
        if (resp.getJson().apps) {
            state.latestStableVersion = resp.getJson().apps[0]?.version
            if (newerVersionExists(state.latestStableVersion, app.version())) {
                if (!state.newVerLogged) {
                   logger ("${app.label} new version ${state.latestStableVersion} is available.","info")
                   state.newVerLogged = true
                }
            } else {
                state.newVerLogged = false
            }
        }
    }
}

void versionCheck() {
    state.latestStableVersion = null
    httpAsyncGet('versionCb',versionUrl,null,null)
}

Boolean updateIfChanged(device, attr, value, delta = null) {
    def currentValue = null
    if (state.currentAttrValue == null) {
        state.currentAttrValue = [:]
    }
    if (state.currentAttrValue[attr] != null) {
        currentValue = state.currentAttrValue[attr].toString()
    }
    Boolean deltaMet = (currentValue == null || value != null && delta != null && Math.abs((value.toInteger() - currentValue.toInteger()).toInteger()) > delta.toInteger())
    Boolean changed = value != null && value != '' && currentValue != null && currentValue != '' && value.toString() != currentValue.toString() && (!delta || deltaMet)
    logger ("${attr} is: ${value} was: ${currentValue} changed: ${changed}","trace")
    Boolean heartBeatUpdateDue = false

    if (state.lastHeartbeatUpdateTime == null) {
         state.lastHeartbeatUpdateTime = [:]
    }  
    if (state.lastHeartbeatUpdateTime[attr] == null || now() - state.lastHeartbeatUpdateTime[attr] > 3600000) {
        heartBeatUpdateDue = true
    }
    if (changed || heartBeatUpdateDue || (currentValue == null && (value != null && value != ''))) {
        state.currentAttrValue[attr] = value.toString()
        state.lastHeartbeatUpdateTime[attr] = now()
        if (device) {
            device.sendEvent(name: attr, value: value)
        } else {
           logger("No Powerwall device to update ${attr} to ${value}","warn")
        }
    }
    return changed
}

void processAboveTriggerDeviceActions() {
    if (aboveTriggerDevicesToOn?.size()) {
        aboveTriggerDevicesToOn.on()
    }
}

void processBelowTriggerDeviceActions() {
    if (belowTriggerDevicesToOff?.size()) {
        belowTriggerDevicesToOff.off()
    }
}

void checkBatteryNotifications(data) {
   if (notifyWhenReserveApproached?.toBoolean() && data.reservePercent != null) {
        reservePct = data.reservePercent.toInteger()
        if (reservePct != 100 && data.batteryPercent - reservePct < 5) {
            String status
            if (data.batteryPercent <= reservePct) {
                status = "is at or below"
            } else {
                status = "is approaching"
            }
            if (state.timeOfLastReserveNotification == null) {
                state.timeOfLastReserveNotification = now()
                sendNotificationMessage(
                    "Powerwall battery level of ${Math.round(data.batteryPercent*10)/10}% ${status} ${reservePct}% reserve level.")
            }
        } else if (state.timeOfLastReserveNotification != null && now() - state.timeOfLastReserveNotification >= 30 * 60 * 1000) {
            //reset for new notification if alert condition no longer exists and it's been at least 30 minutes since last notification
            state.timeOfLastReserveNotification = null
        }
    }

    if (aboveTriggerValue) {
        if (data.batteryPercent >= aboveTriggerValue.toFloat()) {
            if (state.timeOfLastAboveTrigger == null) {
                if (triggerPeriodActive() && aboveTriggerEnabled) {
                    state.timeOfLastAboveTrigger = now()
                    String triggerMessage = "Powerwall ${Math.round(data.batteryPercent*10)/10}% battery level is at or above ${aboveTriggerValue}% trigger."
                    if (actionsValid(aboveTriggerMode, aboveTriggerReserve, aboveTriggerStormwatch, aboveTriggerGridCharging, aboveTriggerDevicesToOn, aboveTriggerGridStatus)) {
                        String message = commandPwActions(aboveTriggerMode, aboveTriggerReserve, aboveTriggerStormwatch, aboveTriggerGridCharging, null, aboveTriggerGridStatus)
                        if (aboveTriggerDevicesToOn?.size() > 0) {
                            message = message + " Turning on devices."
                            runIn(1, processAboveTriggerDeviceActions)
                        }
                        triggerMessage = triggerMessage + " Performing actions. " + message
                    }
                    if (notifyOfSchedules?.toBoolean()) {
                        sendNotificationMessage(triggerMessage)
                    }
                }
            }
        } else if (state.timeOfLastAboveTrigger != null && now() - state.timeOfLastAboveTrigger >= 30 * 60 * 1000) {
            //reset for new trigger if condition no longer exists and it's been at least 30 minutes since last trigger
            state.timeOfLastAboveTrigger = null
        }
    }

    if (belowTriggerValue) {
        if (data.batteryPercent <= belowTriggerValue.toFloat()) {
            if (state.timeOfLastBelowTrigger == null) {
                if (triggerPeriodActive() && belowTriggerEnabled) {
                    state.timeOfLastBelowTrigger = now()
                    String triggerMessage = "Powerwall ${Math.round(data.batteryPercent*10)/10}% battery level is at or below ${belowTriggerValue}% trigger."
                    if (actionsValid(belowTriggerMode, belowTriggerReserve, belowTriggerStormwatch, belowTriggerGridCharging, belowTriggerDevicesToOff, belowTriggerGridStatus)) {
                        String message = commandPwActions(belowTriggerMode, belowTriggerReserve, belowTriggerStormwatch, belowTriggerGridCharging, null, belowTriggerGridStatus)
                        if (belowTriggerDevicesToOff?.size() > 0) {
                            message = message + " Turning off devices."
                            runIn(1, processBelowTriggerDeviceActions)
                        }
                        triggerMessage = triggerMessage + " Performing actions. " + message
                    }
                    if (notifyOfSchedules?.toBoolean()) {
                        sendNotificationMessage(triggerMessage)
                    }
                }
            }
        } else if (state.timeOfLastBelowTrigger != null && now() - state.timeOfLastBelowTrigger >= 30 * 60 * 1000) {
            //reset for new trigger if condition no longer exists and it's been at least 30 minutes since last trigger
            state.timeOfLastBelowTrigger = null
        }
    }
}

String getTileStr(Float zoomLevel) {
    String tileStr = ""
    if (gatewayTileAddress) {
      long width = tileWidth?.toLong() ?: 460
      long height = tileHeight?.toLong() ?: 517  
      float frameScale = zoomLevel?.toFloat() ?: 0.81
      String innerDivStyle = "overflow: hidden; transform: scale(${frameScale}); transform-origin: 0 0; border: none; padding: 0; margin: 0;" 
      String outerDivStyle = "height: ${(height*frameScale).toLong()}px; width: ${width-16}px; overflow: hidden; border: none; padding: 0; margin: 0;"     
      String iframeStyle   = "height: ${height}px; width: ${width}px; border: none; scrollbar-width: none; overflow: hidden; border: none; padding: 0; margin: 0;"  
      tileStr = "<div style = '$outerDivStyle'><div style = '$innerDivStyle'><iframe style='${iframeStyle}' scrolling='no' src='http://${gatewayTileAddress}'></iframe></div></div>"  
    } else {
        tileStr = "Gateway address not entered"
    }
    return tileStr
}

void processGwMeterResponse(response, callData) {
    logger ("processing gateway meter aggregate response","debug")
    if (!response.hasError()) {
        def data = response.json
        logger ("Gw meter agg: ${data}","trace") 
        def child = getPwDevice()
        Integer powerDelta = settings.powerThreshold?.toInteger() ?: 100
        if (updateIfChanged(child, "loadPower", data.load.instant_power.toInteger(), powerDelta) | 
           updateIfChanged(child, "gridPower", data.site.instant_power.toInteger(), powerDelta) | 
           updateIfChanged(child, "power", data.site.instant_power.toInteger(), powerDelta) |
           updateIfChanged(child, "solarPower", data.solar.instant_power.toInteger(), powerDelta) |
           updateIfChanged(child, "powerwallPower", data.battery.instant_power.toInteger(), powerDelta)) 
        {
            child.refreshChildDevices()
        }
    } else {
        logger ("Error procesing gateway meter data: ${response.getStatus()} ${response.getErrorMessage()}","warn")
        if (response.getStatus() == 401 || response.getStatus() == 403) {
            runIn (5, reVerifyGateway)
        }
    }       
}

Float scaleGatewayBatteryPercent (Float percent) {
    Float scaled = (percent - 5.0)/0.95   //adjust TEG to match Tesla Server API. Remove 5% and rescale 0 - 100%
    return Math.round(scaled * 10)/10     //rounded to one decimal place 
}

void processGwSoeResponse(response, callData) {
    logger ("processing gateway SOE response", "debug")
    if (!response.hasError()) {
        def data = response.json
        logger ("Gw SOE: ${data}","trace")
        def child = getPwDevice()
        Float batteryPercent = scaleGatewayBatteryPercent(data.percentage) //adjust TEG to match Tesla Server API
        updateIfChanged(child, "battery", (batteryPercent + 0.5).toInteger())
        updateIfChanged(child, "batteryPercent", batteryPercent)
        runIn(1, checkBatteryNotifications, [data: [batteryPercent: batteryPercent, reservePercent: null]])
    } else { 
        logger ("Error procesing gateway SOE: ${response.getStatus()} ${response.getErrorMessage()}","warn")
    }  
}

def processGwFullStatusResponse(response, callData) {
   logger ("processing gateway full status response","debug")
   if (!response.hasError()) {
       def data = response.json
       logger ("Gw Full status: ${data}","trace")
       def child = getPwDevice()
       updateIfChanged(child, "currentCapacity", data.nominal_full_pack_energy.toInteger())
   } else {
       logger ("Error procesing gateway full status: ${response.getStatus()} ${response.getErrorMessage()}","warn")
   }      
}

void processGwOpResponse(response, callData) {
    logger ("processing gateway operation response","debug")
    if (!response.hasError()) {
        def data = response.json
        logger ("Gw OP: ${data}","trace")
        Float reservePercent = scaleGatewayBatteryPercent(data.backup_reserve_percent) //adjust TEG to match Tesla Server API
        updateOpModeAndReserve(data.real_mode, (reservePercent + 0.5).toInteger()) 
    } else { 
        logger ("Error procesing gateway operation: ${response.getStatus()} ${response.getErrorMessage()}","warn")
    }     
}

void processGwSiteNameResponse(response, callData) {
    logger ("processing gateway sitename response","debug")
    if (!response.hasError()) {
        def data = response.json
        logger ("Gw Site Name: ${data}","trace")
        def child = getPwDevice()
        updateIfChanged(child, "siteName", data.site_name.toString())
    } else {
        logger ("Error procesing gateway sitename: ${response.getStatus()} ${response.getErrorMessage()}","warn")
        
    }      
}

def processGwStatusResponse(response, callData) {
    logger ("processing gateway status response","debug")
    if (!response.hasError()) {
        def data = response.json
        logger ("Gw Status: ${data}","trace")
        updateVersion (data.version) 
    } else {
        logger ("Error procesing gateway status: ${response.getStatus() ${response.getErrorMessage()}}","warn")
    }      
}

def processGwGridStatResponse(response, callData) {
    logger ("processing gateway grid status response","debug")
    if (!response.hasError()) {
        def data = response.json
        updateGridStatus(data.grid_status)
    } else {
        logger ("Error procesing gateway grid status: ${response.getStatus()} ${response.getErrorMessage()}","warn")
    }      
}

void updateGridStatus(String gridStatus) {
    //Server: Active, Inactive, Unknown
    //Gateway: SystemGridConnected, SystemIslandedActive, SystemTransitionToGrid
    if (gridStatus) {
        String gridStatusEnum
        switch (gridStatus) {
            case "Active":
            case "SystemGridConnected":
                gridStatusEnum = "onGrid"
                break
            case "Inactive":
            case "SystemIslandedActive":
                gridStatusEnum = "offGrid"
                break
            case "SystemTransitionToGrid":
            case "Unknown":
                break // No status change    
            default:
               sendNotificationMessage("Powerwall Manager received unexpected grid status: ${gridStatus}", "anomaly")
               break
        }  
        if (gridStatusEnum) {
            Boolean changed = updateIfChanged(getPwDevice(), "gridStatus", gridStatusEnum)
            if (changed) {
                if (gridStatusEnum == "offGrid") {
                    runIn(1, processOffGridActions)
                } else {
                    runIn(1, processOnGridActions)
                }
            }
        }
    }
}

void updateOpModeAndReserve(String opMode, def reservePercent) {
    def pwDevice = getPwDevice()
    Boolean reserveChanged
    Boolean opModeChanged
    if (reservePercent || reservePercent == 0) { //protect against null/bad data
        reserveChanged = updateIfChanged(pwDevice, "reservePercent", reservePercent)
        updateIfChanged(pwDevice, "level", reservePercent)
        updateIfChanged(pwDevice, "reservePercent", reservePercent)
        updateIfChanged(pwDevice, "reserve_pending", reservePercent)
    }
    if (opMode) {
        String opModePretty
        if (opMode == "autonomous") {
            opModePretty = "Time-Based Control"
        } else if (opMode == "self_consumption") {
            opModePretty = "Self-Powered"
        } else if (opMode == "backup") {
            opModePretty = "Backup-Only" //deprecated
        } else {
            opModePretty = opMode
            logger ("Unrecognized Op Mode: ${opMode}","info")
        } 
        opModeChanged = updateIfChanged(pwDevice, "currentOpState", opModePretty)
        if (opModeChanged && notifyWhenModesChange?.toBoolean()) {
            sendNotificationMessage("Powerwall op mode changed to ${opModePretty}")
        }
    }
   
    if (reserveChanged || opModeChanged) {
        pwDevice.refreshChildDevices()
    }
}

void updateVersion(String version) {
    if (version != null) {
        def pwDevice = getPwDevice()
        String lastVersion = pwDevice.currentValue("pwVersion")
        Boolean changed = updateIfChanged(pwDevice, "pwVersion", 'V' + version)
        if (changed && notifyWhenVersionChanges?.toBoolean()) {
            String msg = "Powerwall software version changed to 'V${version}'."
            if (lastVersion) {
                msg = "${msg} Prior version was '${lastVersion}'"
                if (state.lastVerChangeDate) {
                    msg = "${msg} (${state.lastVerChangeDate})."
                } else {
                    msg = "${msg}."
                }
            }
            state.lastVerChangeDate = "${new Date()}"
            sendNotificationMessage(msg)
       }
    }
}

void updateOptimizationStrategy(String strategy) {
    if (strategy) {
        String strategyUi
        if (strategy == "economics") {
            strategyUi = "Cost-Saving"
        } else if (strategy == "balanced") {
            strategyUi = "Balanced"
        } else {
            logger ("Unrecognized Strategy: ${strategy}","info")
            strategyUi = strategy
        }
        state.strategy = strategyUi.toString()
        Boolean changed = updateIfChanged(pwDevice, "currentStrategy", strategyUi)
        if (changed && notifyWhenModesChange?.toBoolean()) {
            sendNotificationMessage("Powerwall ATC optimization strategy changed to ${strategyUi}")
        }
    }
}
    
def processSiteInfoResponse(response, callData) {
    logger ("processing server site info response","debug")
    if (!response.hasError()) {
        def data = response.json.response
        logger ("Site Info: ${data}","trace")
        updateOptimizationStrategy (data?.tou_settings?.optimization_strategy)
        if (data?.tou_settings?.schedule && notifyWhenModesChange?.toBoolean() && state.lastSchedule && data.tou_settings.schedule != state.lastSchedule) {
            sendNotificationMessage("Powerwall Advanced Time Controls schedule has changed")
        }
        if (data.tou_settings?.schedule) {
            state.lastSchedule = data.tou_settings.schedule
        }
        updateVersion (data.version)    
        updateOpModeAndReserve(data.default_real_mode, data.backup_reserve_percent?.toInteger()) 
        def child = getPwDevice()
        if (data.user_settings?.storm_mode_enabled != null) {
            Boolean changed = updateIfChanged(child, "stormwatch", data.user_settings.storm_mode_enabled.toBoolean()) 
            if (changed) {
                child.refreshChildDevices()
            }
        }
        if (data.components?.customer_preferred_export_rule != null) {
            updateIfChanged(child, "energyExportMode", data.components.customer_preferred_export_rule)
        }

        if (data.components?.disallow_charge_from_grid_with_solar_installed != null) {
            updateIfChanged(child, "gridChargingEnabled", !data.components.disallow_charge_from_grid_with_solar_installed)
        } else {
            //Grid Charging with solar status status only appears if disabled..
            updateIfChanged(child, "gridChargingEnabled", true)
        }
        updateIfChanged(child, "siteName", data.site_name.toString())
    } else {
        Integer status = response.getStatus()
        if (status == 401) {
            //log.warn "Site resp error: ${response.getErrorMessage()}."
            runIn (1, handleServerAuthIssue)
        }
        if (callData?.attempt && callData.attempt < 2) {
            logger ("Site response error on attempt ${callData?.attempt}: ${response.getErrorMessage()}. Retrying...","debug")
            runIn(20, requestSiteInfo, [data: [attempt: callData.attempt + 1]])
        } else {
            logger ("Site response error after ${callData?.attempt} attempts: ${status} ${response.getErrorMessage()}.","warn")
        }
    }
}

def processSiteLiveStatusResponse(response, callData) {
    logger ("processing server site live status response","debug")
    if (!response.hasError()) {
        def data = response.json.response
        logger ("Site Live Status: ${data}","trace")
        Boolean stormwatchMode = data?.storm_mode_active
        def child = getPwDevice()
        Boolean stormwatchChanged = updateIfChanged(child, "stormwatchActive", data?.storm_mode_active) 
        if (stormwatchChanged && settings.notifyWhenStormwatch) {
            if (stormwatchMode) {
                sendNotificationMessage("Powerwall Storm Watch mode is active.")
            } else {
                sendNotificationMessage("Powerwall Storm Watch is no longer active.")
            }
        }
   
        if (data.total_pack_energy > 1) //sometimes data appears invalid
        {
            float batteryPercent = data.energy_left.toFloat() / data.total_pack_energy.toFloat() * 100.0
            float bpRounded = Math.round(batteryPercent * 10)/10 //rounded to one decimal place 
            updateIfChanged(child, "battery", (bpRounded + 0.5).toInteger())
            updateIfChanged(child, "batteryPercent", bpRounded)
            if (!connectedToGateway()) {
                //Gateway value may be slightly different from server. Need to verify. Use gateway value only for now if available. 
                updateIfChanged(child, "currentCapacity", data.total_pack_energy.toInteger())
            }
          
            runIn(1, checkBatteryNotifications, [data: [batteryPercent: bpRounded, reservePercent: child.currentValue("reservePercent")]])
        } else if(data.percentage_charged != null) {    // Added by NeilR 3/23/2024 - duplicated above and edited
            logger ("Using 'percentage_charged' for battery level","debug")
            float batteryPercent = data.percentage_charged.toFloat()
            float bpRounded = Math.round(batteryPercent * 10)/10 //rounded to one decimal place 
            updateIfChanged(child, "battery", (bpRounded + 0.5).toInteger())
            updateIfChanged(child, "batteryPercent", bpRounded)
            if (!connectedToGateway()) {
                //Gateway value may be slightly different from server. Need to verify. Use gateway value only for now if available. 
                // //updateIfChanged(child, "currentCapacity", data.total_pack_energy.toInteger())
            }
            runIn(1, checkBatteryNotifications, [data: [batteryPercent: bpRounded, reservePercent: child.currentValue("reservePercent")]])        
        }     
        
        Integer powerDelta = settings.powerThreshold?.toInteger() ?: 100
        Boolean powerChanged = 
           updateIfChanged(child, "loadPower", data.load_power.toInteger(), powerDelta) |
           updateIfChanged(child, "gridPower", data.grid_power.toInteger(), powerDelta) |
           updateIfChanged(child, "power", data.grid_power.toInteger(), powerDelta) |
           updateIfChanged(child, "solarPower", data.solar_power.toInteger(), powerDelta) |
           updateIfChanged(child, "powerwallPower", data.battery_power.toInteger(), powerDelta)
        
        if (stormwatchChanged || powerChanged)
        {
           child.refreshChildDevices()
        }
        
        if (!connectedToGateway()) {
            //Do not update if connected to gateway, to prevent status data thrashing
            updateGridStatus (data.grid_status)
        }
        state.lastCompletedTime = now()
    } else {
        if (status != 401) {
            if (callData?.attempt && callData.attempt < 2) {
                runIn(30, requestSiteLiveStatus, [data: [attempt: callData.attempt + 1]])
            } else {
                logger ("Site live status response error after ${callData?.attempt} attempts: ${response.getStatus()} ${response.getErrorMessage()}.","warn")
            }
        }
    }
}

void processOffGridActions() {
    logger ("processing off grid actions","debug")
    def child = getPwDevice()
    updateIfChanged(child, "switch", "off")
    updateIfChanged(child, "contact", "open")
    if (notifyWhenGridStatusChanges?.toBoolean()) {
        sendNotificationMessage("Powerwall status changed to: Off Grid")
    }
    if (devicesToOffDuringOutage?.size()) {
        devicesToOffDuringOutage.off()
    }
}

void processOnGridActions() {
    logger ("processing on grid actions","debug")
    def child = getPwDevice()
    updateIfChanged(child, "switch", "on")
    updateIfChanged(child, "contact", "closed")
    if (notifyWhenGridStatusChanges?.toBoolean()) {
        sendNotificationMessage("Powerwall status changed to: On Grid")
    }
    if (devicesToOffDuringOutage?.size() && turnDevicesBackOnAfterOutage?.toBoolean()) {
        devicesToOffDuringOutage.on()
    }
    if (devicesToOnAfterOutage?.size()) {
        devicesToOnAfterOutage.on()
    }
}

void requestSiteInfo(data) {
    if (!state?.lastSiteInfoRequestTime || now() - state.lastSiteInfoRequestTime > 1000) {
        Integer tryCount = data?.attempt ?: 1
        if (state.serverVerified) {
            httpAuthAsyncGet('processSiteInfoResponse', "/api/1/energy_sites/${state.energySiteId}/site_info", tryCount)
        }
        state.lastSiteInfoRequestTime = now()
    }
}

void requestSiteLiveStatus(data) {
    if (!state?.lastSiteLiveStatusRequestTime || now() - state.lastSiteLiveStatusRequestTime > 1000) {
        Integer tryCount = data?.attempt ?: 1
        if (state.serverVerified) {
            httpAuthAsyncGet('processSiteLiveStatusResponse', "/api/1/energy_sites/${state.energySiteId}/live_status", tryCount)
        }
        state.lastSiteLiveStatusRequestTime = now()
    }
}

void reVerifyGateway() {
    getLocalGwStatus()
}   

void requestGatewayMeterData() {
     String gwUri = "https://${gatewayAddress}"
     asynchttpGet(processGwMeterResponse, [uri: gwUri, path: "/api/meters/aggregates", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true])
}

void requestGatewaySiteData() {
    String gwUri = "https://${gatewayAddress}"
    asynchttpGet(processGwSoeResponse, [uri: gwUri, path: "/api/system_status/soe", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true])
    asynchttpGet(processGwSiteNameResponse, [uri: gwUri, path: "/api/site_info/site_name", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true])
    asynchttpGet(processGwGridStatResponse, [uri: gwUri, path: "/api/system_status/grid_status", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true])
    asynchttpGet(processGwFullStatusResponse, [uri: gwUri, path: "/api/system_status", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true])

    if (!connectedToTeslaServer()) {   
       //Only process if not connected to the Tesla server to prevent data thrashing
       asynchttpGet(processGwOpResponse, [uri: gwUri, path: "/api/operation", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true])
       asynchttpGet(processGwStatusResponse, [uri: gwUri, path: "/api/system/update/status", headers: gwHeader(), contentType: 'application/json', ignoreSSLIssues: true])
    }   
}

void commandOpMode(data) {
    httpAuthPost(body: [default_real_mode: data.mode], "${data.mode} mode", "/api/1/energy_sites/${state.energySiteId}/operation", {
        resp ->
        //log.debug "${resp.data}"
    })
    runIn(2, requestSiteInfo)
    runIn(30, processWatchdog)
}

void setSelfPoweredMode(child) {
    /* if (child) {  
        def pwDevice = getPwDevice()
        if (pwDevice.currentValue("currentOpState") != "Self-Powered") {
            updateIfChanged(pwDevice, "currentOpState", "Pending Self-Powered") 
        }
    } */
    runIn(1, commandOpMode, [data: [mode: "self_consumption"]])
}

void setTimeBasedControlMode(child) {
    /*if (child) {
        def pwDevice = getPwDevice()
        if (pwDevice.currentValue("currentOpState") != "Time-Based Control") {
            updateIfChanged(pwDevice, "currentOpState", "Pending Time-Based") 
        }
    }*/
    runIn(1, commandOpMode, [data: [mode: "autonomous"]])
}

void setBackupOnlyMode(child) {
    //Deprecated
    /*if (child) {
        child.sendEvent(name: "currentOpState", value: "Pending Backup-Only", displayed: false)
        updateIfChanged(child, "currentOpState", "Pending Backup-Only")
    } */
    runIn(1, commandOpMode, [data: [mode: "backup"]])
}

void commandTouStrategy(data) {
    logger ("commanding TOU strategy to ${data.strategy}","debug")
    //request Site Data to get a current tbc schedule. Schedule needs to be sent on tou strategy command or else schedule will be re-set to default
    def latestSchedule
    try {
        httpAuthGet("/api/1/energy_sites/${state.energySiteId}/site_info", {
            resp ->
            //log.debug "${resp.data}"
            if (resp?.data?.response?.tou_settings?.schedule) {
                latestSchedule = resp.data.response.tou_settings.schedule
            }
        })
    } catch (Exception e) {
        log.debug "Exception ${e} getting latest schedule"
    }
    if (latestSchedule == null) {
       //log.debug "setting latest schedule to last known state"
       latestSchedule = state.lastSchedule
    }

    def commands = [tou_settings: [optimization_strategy: data.strategy, schedule: latestSchedule]]
    httpAuthPost(body: commands, "${data.strategy}", "/api/1/energy_sites/${state.energySiteId}/time_of_use_settings", {
        resp -> //log.debug "${resp.data}"
    })
    runIn(2, requestSiteInfo)
    runIn(30, processWatchdog)
}

void setTbcBalanced(child) {
    if (child) {
        if (child.currentValue("currentStrategy") != "Balanced") {
            updateIfChanged(child, "currentStrategy", "Pending Balanced") 
        }
    }
    runIn(2, commandTouStrategy, [data: [strategy: "balanced"]])
}

void setTbcCostSaving(child) {
    if (child) {
        if (child.currentValue("currentStrategy") != "Cost-Saving") {
            updateIfChanged(child, "currentStrategy", "Pending Cost-Saving") 
        }
    }
    runIn(2, commandTouStrategy, [data: [strategy: "economics"]])
}

void commandBackupReservePercent(data) {
    httpAuthPost(body: [backup_reserve_percent: data.reservePercent], "reserve ${data.reservePercent}%",
    "/api/1/energy_sites/${state.energySiteId}/backup", {
        resp ->
    })
    runIn(2, requestSiteInfo)
    runIn(30, processWatchdog)
}

void commandGoOffGrid(data) {
    if (!connectedToGateway()) {
        logger ("Not connected to gateway, cannot set GoOffGrid Status")
        return
    }
    try
    {
        def islandingMode = "backup"
        logger ("commanding GoOffGrid data is ${data.isOnGrid}","debug")
        if (!data.isOnGrid) {
            islandingMode = "intentional_reconnect_failsafe"   
        }
        logger ("commanding GoOffGrid strategy to ${islandingMode}","debug")
        httpPost([uri: "https://${gatewayAddress}", path: "/api/v2/islanding/mode", headers: gwHeader(), body:"{\"island_mode\":\"${islandingMode}\"}", contentType: 'application/json', ignoreSSLIssues: true]) {
                        response -> 
                        logger("local islanding call successful","debug")
                        logger("response ${response}","debug")
                      }
    
        runIn(2, requestSiteLiveStatus)
        runIn(30, processWatchdog)
    } catch (Exception e) {
        logger ("Error setting local gateway island status: ${e}","warn")
        state.gatewayStatusStr = "Error accessing local gateway.\n" + "Please verify your gateway address and password. ${e}" 
    }
}

void goOffGrid(child){
    logger ("commanding go off grid","debug")
    runIn(2, commandGoOffGrid, [data: [isOnGrid:false]])
}

void goOnGrid(child){
    logger ("commanding go on grid","debug")
    runIn(2, commandGoOffGrid, [data: [isOnGrid:true]])
}

void setBackupReservePercent(child, value) {
    if (value != null && value.toInteger() >= 0 && value.toInteger() <= 100) {
        runIn(2, commandBackupReservePercent, [data: [reservePercent: value.toInteger()]])
    } else {
        log.debug "Backup reserve percent of: ${value} not sent. Must be between 0 and 100"
    }
}

void commandStormwatchEnable() {
    httpAuthPost(body: [enabled: true], "stormwatch mode enable", "/api/1/energy_sites/${state.energySiteId}/storm_mode", {
        resp -> //log.debug "${resp.data}"
    })
    runIn(3, requestSiteInfo)
    runIn(30, processWatchdog)
}

void commandStormwatchDisable() {
    httpAuthPost(body: [enabled: false], "stormwatch mode disable", "/api/1/energy_sites/${state.energySiteId}/storm_mode", {
        resp -> //log.debug "${resp.data}"
    })
    runIn(2, requestSiteInfo)
    runIn(30, processWatchdog)
}

void enableStormwatch(child) {
    logger ("commanding stormwatch on","debug")
    runIn(2, commandStormwatchEnable)
}

void disableStormwatch(child) {
    logger ("commanding stormwatch off","debug")
    runIn(2, commandStormwatchDisable)
}

void commandGridChargingEnable() {
    httpAuthPost(body: [disallow_charge_from_grid_with_solar_installed: false], "grid charging enable", "/api/1/energy_sites/${state.energySiteId}/grid_import_export", {
        resp -> //log.debug "${resp.data}"
    })
    runIn(2, requestSiteInfo) 
    runIn(30, processWatchdog)
}

void commandGridChargingDisable() {
    httpAuthPost(body: [disallow_charge_from_grid_with_solar_installed: true], "grid charging disable", "/api/1/energy_sites/${state.energySiteId}/grid_import_export", {
        resp -> //log.debug "${resp.data}"
    })
    runIn(2, requestSiteInfo) 
    runIn(30, processWatchdog)
}

void enableGridCharging(child) {
    logger ("commanding grid charging on","debug")
    runIn(2, commandGridChargingEnable)
}

void disableGridCharging(child) {
    logger ("commanding grid charging off","debug")
    runIn(2, commandGridChargingDisable)
}

void commandEnergyExportModeSolarOnly() {
    httpAuthPost(body: [customer_preferred_export_rule: "pv_only"], "energy export mode solar-only", "/api/1/energy_sites/${state.energySiteId}/grid_import_export", {
        resp -> //log.debug "${resp.data}"
    })
    runIn(3, requestSiteInfo)
    runIn(30, processWatchdog)
}

void commandEnergyExportModeEverything() {
    httpAuthPost(body: [customer_preferred_export_rule: "battery_ok"], "energy export mode everything", "/api/1/energy_sites/${state.energySiteId}/grid_import_export", {
        resp -> //log.debug "${resp.data}"
    })
    runIn(2, requestSiteInfo)
    runIn(30, processWatchdog)
}

void setEnergyExportModeSolarOnly(child) {
    logger ("setting export mode to solar only","debug")
    runIn(2, commandEnergyExportModeSolarOnly)
}

void setEnergyExportModeEverything(child) {
    logger ("setting export mode to battery and solar","debug")
    runIn(2, commandEnergyExportModeEverything)
}







def refresh(child) {
    if (logLevel == "debug" | logLevel == "trace") {
        logger ("refresh requested","debug")
    }
    if (useTessie) { getPowerwalls() }
    
    state.lastHeartbeatUpdateTime = [:]
    runIn(1, processServerMain)
    runIn(2, processGatewayMain)
    runIn(30, processWatchdog)
}

void processWatchdog() {
    def lastTimeProcessed
    def lastTimeCompleted
    if (!state.lastProcessedTime | !state.lastCompletedTime) {
        lastTimeProcessed = now()
        lastTimeCompleted = now()
    } else {
        lastTimeProcessed = state.lastProcessedTime
        lastTimeCompleted = state.lastCompletedTime
    }

    def secondsSinceLastProcessed = (now() - lastTimeProcessed) / 1000
    def secondsSinceLastProcessCompleted = (now() - lastTimeCompleted) / 1000
    def maxDownTime = 1800
    if (pollingPeriod) {
       if (pollingPeriod == "30 minutes") {
          maxDownTime = 6000
       } else if (pollingPeriod == "1 hour") {
          maxDownTime = 8000
       } else if (pollingPeriod == "Do not poll") {
          maxDownTime = 700000
       } 
    }

    if (secondsSinceLastProcessed > maxDownTime) {
        if (!state?.processedWarningSent) {
            String msg = "Powerwall Manager has not executed in ${(secondsSinceLastProcessed/60).toInteger()} minutes. Reinitializing"
            sendNotificationMessage("Warning: " + msg, "anomaly")
            state.processedWarningSent = true
            logger (msg,"warn")
            runIn(30, initialize)
        }
    } else if (secondsSinceLastProcessCompleted > maxDownTime) {
        if (state.serverValidAtStartup) {
           if (!state?.completedWarningSent) {
                String msg = "Powerwall Manager has not successfully received and processed server data in ${(secondsSinceLastProcessCompleted/60).toInteger()} minutes. Reinitializing"
                sendNotificationMessage("Warning: " + msg,"anomaly")
                state.completedWarningSent = true
                logger (msg,"warn")
                runIn(30, initialize)
           }
        }
    } else {
        if (state?.completedWarningSent || state?.processedWarningSent) {
            String msg = "Info: Powerwall Manager has successfully resumed operation"
            sendNotificationMessage(msg, "anomaly")
            state.completedWarningSent = false
            state.processedWarningSent = false
            logger(msg,"info")
        }    
    }
}

void scheduleRefreshAccessToken() {
    if (state.tokenExpiration) {
        Long refreshDateEpoch = state.tokenExpiration - oneHourMs*2.5 // 2.5 hours before expiration
        //Min 1 hour, max 30 days
        if (refreshDateEpoch - now() < oneHourMs) {
            refreshDateEpoch = now() + oneHourMs
        } else if (refreshDateEpoch - now() > oneDayMs*30) {
            refreshDateEpoch = now() + oneDayMs*30
        }
        def refreshDate = new Date(refreshDateEpoch)
        logger ("Scheduling token refresh for ${refreshDate}.","debug")
        runOnce(refreshDate, refreshAccessToken) 
        state.scheduleRefreshToken = false
        state.refreshSchedTime = refreshDateEpoch
    } else {
        logger ("Unable to schedule refresh token. No expiration date found","warn")           
    }
}
    
void processServerMain() {
    //if (!state.forceSrvrFailure) {
    //    log.debug "server fail test initiated"
    //    state.forceSrvrFailure=true
    //    app.updateSetting("inputAccessToken",[type:"text",value:"  "])
    //}   
    state.lastProcessedTime = now()
    def lastStateProcessTime
    if (state.lastStateRunTime == null) {
        lastStateProcessTime = 0
    } else {
        lastStateProcessTime = state.lastStateRunTime
    }
    def secondsSinceLastRun = (now() - lastStateProcessTime) / 1000
    if (secondsSinceLastRun > 60) {
        state.lastStateRunTime = now()
        runIn(1, requestSiteLiveStatus)
        runIn(10, requestSiteInfo)
       
        if ((settings.notifyTokenAge == null || settings.notifyOfTokenAge) && state.tokenChangeTime && !state.tokenAgeWarnSent) {
            Integer tokenAgeDays = ((now() - state.tokenChangeTime)/oneDayMs).toInteger()
            if (tokenAgeDays > 40) {
               state.tokenAgeWarnSent = true
               sendNotificationMessage("Powerwall Manager: Tesla access token was last updated ${tokenAgeDays} days ago.")
            }
        }
        if (state.scheduleRefreshToken && state.tokenExpiration) {
            scheduleRefreshAccessToken()
        }
        
    }
}

void processGatewayMain() {
    //if (!state.forceGwFailure) {
    //    log.debug "gateway fail test initiated"
    //    state.forceGwFailure=true
    //    state.gwAuthCookie = "  "
    //}
    logger ("Processing processGatewayMain","debug")
    if (gatewayAddress) {
        if (state.gatewayVerified) {
            logger ("requesting data from gateway","debug")
            if (state.gatewayConnectFailMode) {
                //was in failure mode, ok now
                unschedule (reVerifyGateway)
                state.gatewayConnectFailMode = false
            }
            runIn (2, requestGatewayMeterData)
            runIn (5, requestGatewaySiteData) 
        } else if (!state.gatewayConnectFailMode) {
            //gateway is not validated, but not yet in failure mode, re-check gateway login
            logger ("gateway not been verified","debug")
            getLocalGwStatus()
            if (state.gatewayVerified) {
                //it's good now
               logger ("gateway now verified","debug")
               runIn (2, requestGatewayMeterData)
               runIn (5, requestGatewaySiteData) 
            } else {
               //Gateway could not be verified. Put in gateway failure mode
               logger ("entering gateway fail mode","debug")
               state.gatewayConnectFailMode = true
               runEvery1Hour (reVerifyGateway) 
            }
        }
    }
}

String validationStrFromCode(Integer theCode) {
    String theString
    switch (theCode) { 
        case 200: 
            theString = "Validated with Tesla"
            break; 
        case 401: 
            theString = "Not Validated with Tesla - Unauthorized"
            break
        default:
            theString = "Not Validated with Tesla - ${codeFromToken} Error" 
            break
    }
    return theString
}
 
void validateTokenFromUrl(String theToken) {
    Integer codeFromToken = statusCodeFromToken(theToken)
    Boolean tokenValid = (codeFromToken == 200) 
    state.accessTokenFromUrlValid = tokenValid
    state.accessTokenFromUrlStatus = validationStrFromCode (codeFromToken)
    if (tokenValid) {
        state.serverFailureMode = false
    }  
    logger ("Token from URL Valid: ${tokenValid}","debug")
}

Boolean validateInputToken() {
    Integer codeFromToken = statusCodeFromToken(inputAccessToken)
    Boolean tokenValid = (codeFromToken == 200) 
    state.inputAccessTokenValid = tokenValid
    state.inputAccessTokenStatus = validationStrFromCode (codeFromToken)
    if (tokenValid || useTessie) {
        state.serverFailureMode = false
    }  
    if (useTessie) {
        logger  ("Input Access Token check skipped - Using Tessie", "debug")
        tokenValid = true
        state.inputAccessTokenValid = true //alan_f
    }
    else
    {
        logger ("Input Access Token Valid: ${tokenValid}","debug")
    }
    return tokenValid
}

def tokenFromUrlCallback (resp){
    state.accessTokenFromUrlStatus = "Received status from URL"
    if (resp.status == 200) {
         state.accessTokenFromUrlStatus = "Received OK status from local URL"
         logger ("Token from URL body = ${resp.body}", "trace")
         def results = new groovy.json.JsonSlurper().parseText(resp.body)  
         logger("Access token from URL received in callback: ${results.access_token}","debug")
         if (results.access_token) {
            state.accessTokenFromUrlStatus = "Received Token from URL"
            String theToken = results.access_token
            state.accessTokenFromUrl = theToken
            validateTokenFromUrl(theToken)
         }
     } else {
         logger ("Token from URL failed with status ${resp.status}","warn")
         state.accessTokenFromUrlStatus = "Token Not received from local URL. Status: ${resp.status}"
     }
}

void validateLocalUrl() {
    // get the token from the local URL, validate with Tesla, and set state status
    state.accessTokenFromUrlValid = false
    def accessTokenFromUrl
    String thePath = accessTokenPath ?: "/"
    if (hubIsSt()) {
      state.accessTokenFromUrlStatus = "Requested from local URL"
      def httpGetAction = physicalgraph.device.HubAction.newInstance(
          method: "GET",
          path: "${thePath}",
          headers: [HOST : "${accessTokenIp}:0080"],
          null,
          [callback: tokenFromUrlCallback])
      sendHubCommand(httpGetAction);
    } else {
        state.accessTokenFromUrlCode = 0
        if (accessTokenUrl) {
           def params = [
                uri: accessTokenUrl,
                contentType : 'application/json'
             ]
           try {
             httpGet(params) { resp ->  
                 Integer code = resp.status
                 if (code == 200) {
                     logger ("Received Access Token from local URL","debug")
                     accessTokenFromUrl = "${resp.data.access_token}"
                     state.accessTokenFromUrl  = accessTokenFromUrl
                     validateTokenFromUrl(accessTokenFromUrl)  
                 } else {
                     logger ("Get Access Token from local URL failed with status ${code}","warn")
                     state.accessTokenFromUrlStatus = "Token Not Received from local URL. Status: ${code}"
                 }    
                 state.accessTokenFromUrlCode = code
            }
          } catch (groovyx.net.http.HttpResponseException e) {
               def statusCode = e?.response?.getStatus()
               logger ("Access token from URL failed with HTTP exception: ${e} code: ${statusCode}","info")
               state.accessTokenFromUrlStatus = "Token Not Received from local URL - ${statusCode} ${e?.response?.getStatusLine()}"
          } catch (Exception e) {
               logger ("Access token from URL failed with general exception: ${e}","info")
               state.accessTokenFromUrlStatus = "Token Not Received from local URL. General exception on call"
          }
        } else {
            logger ("Cannot query local server for URL. accessTokenUrl is null", "trace")
        }
    }   
}

Integer statusCodeFromToken (tryToken) {
    Integer statusCode = 0
    String path = "/api/1/products"
    if (tryToken) {
       try {
          httpAuthGet(path, {
             resp ->
                statusCode = resp.status
         }, tryToken)
       } catch (groovyx.net.http.HttpResponseException e) {
           logger ("HTTP exception getting status from ${path} : ${e}","info")
          statusCode = e?.response?.getStatus()
       } catch (Exception e) {
          logger ("General exception getting token status from ${path}: ${e}","info")
       }
   }
   return statusCode
}

Boolean tokenFailover() {
    Boolean success = false
    if (state.inputAccessTokenValid) {
        state.useInputToken = true
        success = true
        logger ("Input token is now valid","debug")
    } else if (state.accessTokenFromUrlValid) {
        state.useTokenFromUrl = true
        //The input token was is no longer valid, but the token from URL is good. Stop using the input token so only the token from URL is considered
        state.useInputToken = false
        success = true
        logger ("Token from URL is now valid","debug")
    } else {
       // both tokens failed, send a notification)
       if (!state.serverFailureMode) {
           state.serverFailureMode = true
           String msg = "Authorization issue connecting to Tesla Server. Please check your tokens in the Powerwall Manager app"
           logger (msg,"error")
           if (state.serverValidAtStartup) {
              sendNotificationMessage("Powerwall Manager: " + msg, "anomaly")
           }
        }
    }
    return success
}

void handleServerAuthIssue() {
    if (!state.serverFailureMode) {
       if (!validateInputToken() && state.accessTokenFromUrlValid) {
          state.useInputToken = false //force use of token from URL
       }
       if (accessTokenIp) {
           validateLocalUrl() //check for a new token from URL
       }
       runIn (3, initialServerFailover)
    }
}

void initialServerFailover() {
    if (!tokenFailover()) {
        //Still no valid tokens
        runIn(3600, prepDailyServerFailover) // in one hour
    }
}

void prepDailyServerFailover() {
    if (accessTokenIp) {
        validateLocalUrl()
    }
    runIn (5, dailyServerFailover)
}

void dailyServerFailover() {
    refreshAccessToken()
    validateInputToken()
    if (!tokenFailover()) {
        //Still no valid tokens
        state.useInputToken = false
        state.useTokenFromUrl = false
        runIn(8600, prepDailyServerFailover()) //continue running a daily failover check
    }
}
        
void logger (String message, String msgLevel="debug") {
    Integer prefLevelInt = settings.logLevel ? logLevels[settings.logLevel] : 4
    Integer msgLevelInt = logLevels[msgLevel]
    if (msgLevelInt >= prefLevelInt && prefLevelInt) {
        log."${msgLevel}" message
    } else if (!msgLevelInt) {
        log.info "${message} logged with invalid level: ${msgLevel}"
    }
}

def hrefMenuPage (String page, String titleStr, String descStr, String image, params, state = null) {
    if (hubIsSt()) {
        href page, title: titleStr, description: descStr, required: false, image: image, params: params, state: state
    } else {
        String imgFloat = ""
        String imgElement = ""
        if (descStr) {imgFloat = "float: left;"} //Center title} if no description
        if (image) {imgElement = "<img src='${image}' width='36' style='${imgFloat} width: 36px; padding: 0 16px 0 0'>"}
        String titleDiv = imgElement + titleStr
        String descDiv = "<div style='float :left; width: 90%'>" + descStr + "</div>"
        href page, description: descDiv, title: titleDiv, required: false, params : params, state : state
    }
}
          
// Constants
@Field static final Map logLevels = ["none":0, "trace":1,"debug":2,"info":3, "warn":4,"error":5]
@Field static final String teslaUrl = "https://owner-api.teslamotors.com"
@Field static final String tessieUrl = "https://api.tessie.com" //alan_f
@Field static final String ddUrl = "https://darwinsden.com/powerwall/"
@Field static final String versionUrl = "https://raw.githubusercontent.com/youzer-name/Tesla-Powerwall-Manager---Tessie/master/packageManifest.json"
@Field static final String teslaBearerTokenEndpoint = "https://auth.tesla.com/oauth2/v3/token"
@Field static final String teslaBearerTokenGrantType = "refresh_token"
@Field static final String teslaBearerTokenClientId = "ownerapi"
@Field static final String teslaBearerTokenScope = "openid email offline_access"
@Field static final String teslaAccessTokenEndpoint = "https://owner-api.teslamotors.com/oauth/token"
@Field static final String teslaAccessTokenAuthGrantType = "urn:ietf:params:oauth:grant-type:jwt-bearer"
@Field static final String teslaAccessTokenAuthClientId = "81527cff06843c8634fdc09e8ac0abefb46ac849f38fe1e431c2ef2106796384"
@Field static final Integer maxSmartThingsSchedules = 15
@Field static final Long oneMinuteMs = 1000*60
@Field static final Long oneHourMs = 1000*60*60
@Field static final Long oneDayMs = 1000*60*60*24


// Icons
@Field static final String teslaIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/Tesla-Icon40.png"
@Field static final String gatewayIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/gateway.png"
@Field static final String notifyIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/notification40.png"
@Field static final String batteryIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/battery40.png"
@Field static final String outageIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/outage40.png"
@Field static final String ddLogoHubitat = "https://darwinsden.com/download/ddlogo-for-hubitat-pwManagerv4-png"
@Field static final String ddLogoSt = "https://darwinsden.com/download/ddlogo-for-st-pwManagerV4-png"
@Field static final String cogIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/cogD40.png"
@Field static final String dashIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/dashboard40.png"
@Field static final String schedIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/schedClock40.png"
@Field static final String schedOkIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/schedOk40.png"
@Field static final String addIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/add40.png"
@Field static final String schedIncomplIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/schedIncompl40.png"
@Field static final String ppBtn = "https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif"
@Field static final String pwLogo = "https://raw.githubusercontent.com/DarwinsDen/Tesla-Powerwall-Manager/main/images/PWLogo.png"
@Field static final String trashIcon = "https://rawgit.com/DarwinsDen/SmartThingsPublic/master/resources/icons/trash40.png"
