# Liferay Themes Switcher portlet

## 1. Introduction

Liferay Themes Switcher portlet is developed by [Aimprosoft](http://www.aimprosoft.com) company and intended to provide 
the possibility for users to apply their own themes and color schemes for sites/organization they want.

_Problem:_ OOTB Liferay provides the possibility to apply themes/color schemes only for administrators. 
And once they apply those settings all users will be able to see the same view, and there is no way for them to customize "look and feel".

_Solution:_  Using a Liferay Themes Switcher portlet users can customize the representation of Liferay pages according to their needs.

## 2. Description

Liferay Themes Switcher portlet has two modes:

1. *VIEW MODE* is available for all users (including guests). It allows users to choose and apply any theme and color scheme for the current site. A list of themes and color schemes displayed for the user is filtered according to the permissions he has, and those permissions are defined by the administrator. When the user chooses theme/color scheme for a site those settings are saved to a custom table in the database, so they are persistent. In the case of a Guest user, settings are saved to a session scope and live during session time.

2. *EDIT MODE* (preferences) is available for administrators only. Administrators can define permissions on themes and color schemes for Liferay roles. Once they define a 'VIEW' permission on the theme for some role users with this role will see this theme in the list and will be able to apply it. Administrators can also reset a theme configuration to the default one (in this case all settings will be cleared from database and session).

## 3. Installation

## 3.1. Maven Installation

For maven installation you need to: 

1) Modify a `liferay.dir` property value with a path to Liferay installation;

2) Run `mvn clean install` command to install the portlet.

## 3.2. Installation with WAR file.

To install portlet with WAR file you need either copy it to Liferay's `deploy` directory, 
or install it from Control Panel (`Control Panel -> Apps -> App Manager -> Install`).

## 3.3. Installation from Liferay MarketPlace.

Download LPKG file from Liferay Markerplace.
Copy it to Liferay's `deploy` directory to auto-deploy it.
