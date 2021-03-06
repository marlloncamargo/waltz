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

package com.khartec.waltz.jobs.harness;

import com.khartec.waltz.data.roadmap.RoadmapDao;
import com.khartec.waltz.model.EntityKind;
import com.khartec.waltz.model.roadmap.RoadmapAndScenarioOverview;
import com.khartec.waltz.service.DIConfiguration;
import com.khartec.waltz.service.roadmap.RoadmapService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;

import static com.khartec.waltz.model.EntityReference.mkRef;


public class RoadmapHarness {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DIConfiguration.class);

        RoadmapDao roadmapDao = ctx.getBean(RoadmapDao.class);
        RoadmapService roadmapService = ctx.getBean(RoadmapService.class);

        Collection<RoadmapAndScenarioOverview> relns = roadmapService
                .findRoadmapsAndScenariosByFormalRelationship(mkRef(EntityKind.ORG_UNIT, 2700));

        System.out.println(relns);


    }

}
