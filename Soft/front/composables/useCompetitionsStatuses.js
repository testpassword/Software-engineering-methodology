const STATUSES = [
  { name: 'ACCEPTATION',       label: 'ПРИНИМАЕТСЯ' },
  { name: 'IN_PROGRESS',       label: 'ВЫПОЛНЯЕТСЯ' },
  { name: 'VOTING',            label: 'ГОЛОСОВАНИЕ' },
  { name: 'WAITING_AGREEMENT', label: 'ОЖИДАЕТ_СОГЛАСИЙ' },
  { name: 'MARRIAGE',          label: 'СВАДЬБА' }
]

export const useCompetitionsStatues = () => ({ STATUSES })
