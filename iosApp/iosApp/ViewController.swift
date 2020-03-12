import UIKit
import app

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = Proxy().proxyHello()
        DispatchQueue.main.async { self.label.text = Sample().callBridge() }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
}
