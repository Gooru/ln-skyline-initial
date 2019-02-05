package org.gooru.skylineinitial.infra.services.ilpcalculator;

import java.util.Set;
import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class IlpCalculatorServiceForDiagnosticPlayed implements IlpCalculatorService {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private final ProcessingContext context;

  IlpCalculatorServiceForDiagnosticPlayed(DBI dbi4core, DBI dbi4ds,
      ProcessingContext context) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
    this.context = context;
  }

  @Override
  public CompetencyLine calculateCompetenciesCompleted() {
    // TODO: Implement this
    return null;
  }
}
