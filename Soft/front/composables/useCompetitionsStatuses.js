const STATUSES = [
  { name: 'IN_PROGRESS',       label: 'ВЫПОЛНЯЕТСЯ' },
  { name: 'VOTING',            label: 'ГОЛОСОВАНИЕ' },
  { name: 'WAITING_AGREEMENT', label: 'ОЖИДАЕТ СОГЛАСИЙ' },
  { name: 'MARRIAGE',          label: 'СВАДЬБА' }
]

export const useCompetitionsStatues = () => ({ STATUSES })
