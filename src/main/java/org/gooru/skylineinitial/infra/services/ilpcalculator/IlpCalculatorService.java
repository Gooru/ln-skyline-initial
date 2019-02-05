package org.gooru.skylineinitial.infra.services.ilpcalculator;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface IlpCalculatorService {

  CompetencyLine calculateCompetenciesCompleted();

  static IlpCalculatorService buildForDiagnosticPlayed(DBI dbi4core, DBI dbi4ds,
      ProcessingContext context) {
    return new IlpCalculatorServiceForDiagnosticPlayed(dbi4core, dbi4ds, context);
  }

  static IlpCalculatorService buildForHeuristicBound(DBI dbi4core, DBI dbi4ds,
      ProcessingContext context) {
    return new IlpCalculatorServiceForHeuristicBound(dbi4core, dbi4ds, context);
  }

}
