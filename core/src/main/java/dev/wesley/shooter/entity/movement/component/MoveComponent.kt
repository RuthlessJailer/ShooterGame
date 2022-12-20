package dev.wesley.shooter.entity.movement.component

data class MoveComponent(var speed : Float = 0f,
                         var cos : Float = 0f,
                         var sine : Float = 0f,
                         var root : Boolean = false
){
}