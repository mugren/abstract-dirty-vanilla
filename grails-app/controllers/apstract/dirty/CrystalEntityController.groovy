package apstract.dirty

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CrystalEntityController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond CrystalEntity.list(params), model:[crystalEntityCount: CrystalEntity.count()]
    }

    def show(CrystalEntity crystalEntity) {
        respond crystalEntity
    }

    def create() {
        respond new CrystalEntity(params)
    }

    @Transactional
    def save(CrystalEntity crystalEntity) {
        if (crystalEntity == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (crystalEntity.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond crystalEntity.errors, view:'create'
            return
        }

        crystalEntity.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'crystalEntity.label', default: 'CrystalEntity'), crystalEntity.id])
                redirect crystalEntity
            }
            '*' { respond crystalEntity, [status: CREATED] }
        }
    }

    def edit(CrystalEntity crystalEntity) {
        respond crystalEntity
    }

    @Transactional
    def update(CrystalEntity crystalEntity) {
        println crystalEntity.dirtyPropertyNames
        if (crystalEntity == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (crystalEntity.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond crystalEntity.errors, view:'edit'
            return
        }

        crystalEntity.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'crystalEntity.label', default: 'CrystalEntity'), crystalEntity.id])
                redirect crystalEntity
            }
            '*'{ respond crystalEntity, [status: OK] }
        }
    }

    @Transactional
    def delete(CrystalEntity crystalEntity) {

        if (crystalEntity == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        crystalEntity.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'crystalEntity.label', default: 'CrystalEntity'), crystalEntity.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'crystalEntity.label', default: 'CrystalEntity'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
