package org.lifetrack.lifetrackspring.services

import org.lifetrack.lifetrackspring.database.delegate.VisitDelegate
import org.lifetrack.lifetrackspring.database.repository.VisitRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    @param:Qualifier("visitDelegateImpl") private val visitDelegate: VisitDelegate
) : VisitDelegate by visitDelegate {


}