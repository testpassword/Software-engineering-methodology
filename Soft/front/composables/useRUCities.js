import cities from 'cities.json'

export const useRUCities = () => cities.filter( it => it.country === 'RU' ).map( it => it.name )
