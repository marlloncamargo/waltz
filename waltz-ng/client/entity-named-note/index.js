/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016  Khartec Ltd.
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

import angular from 'angular';
import {registerComponents} from '../common/module-utils';

import * as entityNamedNoteStore from './services/entity-named-note-store';
import * as entityNamedNoteTypeStore from './services/entity-named-note-type-store';

import entityNamedNotesPanel from './components/panel/entity-named-notes-panel';
import entityNamedNotesSection from './components/section/entity-named-notes-section';


export default () => {

    const module = angular.module('waltz.entity.named-note', []);

    module
        .service(entityNamedNoteStore.serviceName, entityNamedNoteStore.store)
        .service(entityNamedNoteTypeStore.serviceName, entityNamedNoteTypeStore.store);

    registerComponents(module, [
        entityNamedNotesPanel,
        entityNamedNotesSection
    ]);

    return module.name;
};