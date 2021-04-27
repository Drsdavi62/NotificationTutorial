import {Knex} from 'knex'

export async function up(knex : Knex){
    return knex.schema.createTable('tokens', table => {
        table.increments('id').primary();
        table.string('token').notNullable();
    })
}

export async function down (knex : Knex) {
    return knex.schema.dropTable('tokens')
}