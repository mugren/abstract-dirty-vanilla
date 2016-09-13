import apstract.dirty.CrystalEntity

class BootStrap {

    def init = { servletContext ->

        def crystalEntity = CrystalEntity.findByName("crystal_entity")?: new CrystalEntity(
                name: "crystal_entity", location: "Mars")

        crystalEntity.save(flush: true)

        crystalEntity.name = "crystal_entity2"
        println crystalEntity.dirtyPropertyNames
        crystalEntity.location = "Jupiter"
        println crystalEntity.dirtyPropertyNames

    }
    def destroy = {
    }
}
