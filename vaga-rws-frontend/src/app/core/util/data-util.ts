export function getDiasRestantes(data?: string | Date): number | null {
    if (!data) return null;

    let dataObj: Date;

    if (typeof data === 'string') {
        dataObj = new Date(data);
    } else {
        dataObj = data;
    }

    if (isNaN(dataObj.getTime())) return null;

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    const dataAlvo = new Date(dataObj);
    dataAlvo.setHours(0, 0, 0, 0);

    const diff = dataAlvo.getTime() - hoje.getTime();
    const dias = Math.ceil(diff / (1000 * 3600 * 24));
    return dias > 0 ? dias : 0;
}