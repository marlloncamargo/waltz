/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016, 2017 Waltz open source project
 * See README.md for more information
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import roles from "../user/roles";

/**
 * Given an entity kind, returns the user roles required for editing
 *
 * Note this file should correspond to the Java impl:
 * `com.khartec.waltz.service.user.RoleUtilities.getRequiredRoleForEntityKind(...)`
 *
 * @param kind
 * @param secondaryKind  (Optional) secondary kind
 * @returns string  key of role
 */
export function getEditRoleForEntityKind(kind, secondaryKind) {
    switch (kind) {
        case "APPLICATION":
            return roles.APP_EDITOR.key;
        case "CHANGE_INITIATIVE":
            return roles.CHANGE_INITIATIVE_EDITOR.key;
        case "MEASURABLE":
            return secondaryKind
                ? roles.CAPABILITY_EDITOR.key
                : roles.TAXONOMY_EDITOR.key;
        case "ORG_UNIT":
            return roles.ORG_UNIT_EDITOR.key;
        case "MEASURABLE_CATEGORY":
            return secondaryKind
                ? roles.CAPABILITY_EDITOR.key
                : roles.TAXONOMY_EDITOR.key;
        case "DATA_TYPE":
            return roles.AUTHORITATIVE_SOURCE_EDITOR.key;
        case "SCENARIO":
            return roles.SCENARIO_EDITOR.key;
        case "ROADMAP":
            return roles.SCENARIO_ADMIN.key;
        default:
            return roles.ADMIN.key;
    }
}