package ikshare.domain.event.listener;

public interface TransferQueueListener {
	public void activeUploadsChanged();
	public void activeDownloadsChanged();
}
