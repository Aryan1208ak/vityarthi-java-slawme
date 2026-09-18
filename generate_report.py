import os
import sys
from reportlab.lib import colors
from reportlab.lib.pagesizes import letter
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, KeepTogether, HRFlowable
)
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_JUSTIFY, TA_RIGHT
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_header_footer(num_pages)
            super().showPage()
        super().save()

    def draw_header_footer(self, page_count):
        if self._pageNumber == 1:
            return  # Skip cover page
        self.saveState()
        self.setFont("Helvetica-Bold", 8)
        self.setFillColor(colors.HexColor("#1A2B4C"))
        self.drawString(54, 11 * 72 - 36, "VITyarthi Flipped Course Evaluation — Programming in Java")
        self.setFont("Helvetica", 8)
        self.setFillColor(colors.HexColor("#666666"))
        self.drawRightString(8.5 * 72 - 54, 11 * 72 - 36, "SLAWME Project Report")

        self.setStrokeColor(colors.HexColor("#CCCCCC"))
        self.setLineWidth(0.5)
        self.line(54, 11 * 72 - 42, 8.5 * 72 - 54, 11 * 72 - 42)

        # Footer
        self.line(54, 46, 8.5 * 72 - 54, 46)
        self.setFont("Helvetica", 8)
        self.drawString(54, 32, "Confidential & Academic Submission | Smart Logistics Engine")
        page_text = f"Page {self._pageNumber} of {page_count}"
        self.drawRightString(8.5 * 72 - 54, 32, page_text)
        self.restoreState()

def create_pdf_report(filename="Project_Report.pdf"):
    doc = SimpleDocTemplate(
        filename,
        pagesize=letter,
        leftMargin=54,
        rightMargin=54,
        topMargin=54,
        bottomMargin=54
    )

    styles = getSampleStyleSheet()
    
    # Custom Palette
    PRIMARY = colors.HexColor("#1A365D")   # Deep Navy
    SECONDARY = colors.HexColor("#2B6CB0") # Slate Blue
    ACCENT = colors.HexColor("#319795")    # Teal
    DARK_BG = colors.HexColor("#2D3748")
    LIGHT_BG = colors.HexColor("#F7FAFC")
    BORDER_COLOR = colors.HexColor("#E2E8F0")

    title_style = ParagraphStyle(
        'CoverTitle',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=24,
        leading=28,
        textColor=PRIMARY,
        alignment=TA_CENTER,
        spaceAfter=15
    )

    subtitle_style = ParagraphStyle(
        'CoverSubtitle',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=13,
        leading=18,
        textColor=SECONDARY,
        alignment=TA_CENTER,
        spaceAfter=30
    )

    h1_style = ParagraphStyle(
        'Heading1_Custom',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=15,
        leading=19,
        textColor=PRIMARY,
        spaceBefore=14,
        spaceAfter=8,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'Heading2_Custom',
        parent=styles['Heading2'],
        fontName='Helvetica-Bold',
        fontSize=11,
        leading=15,
        textColor=SECONDARY,
        spaceBefore=10,
        spaceAfter=6,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body_Custom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=13.5,
        textColor=colors.HexColor("#2D3748"),
        alignment=TA_LEFT,
        spaceAfter=6
    )

    code_style = ParagraphStyle(
        'Code_Custom',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=8,
        leading=10.5,
        textColor=colors.HexColor("#1A202C"),
        spaceBefore=4,
        spaceAfter=4
    )

    story = []

    # ==================== 1. COVER PAGE ====================
    story.append(Spacer(1, 40))
    story.append(Paragraph("VITyarthi — Build Your Own Project", subtitle_style))
    story.append(Paragraph("Smart Logistics & Automated Warehouse<br/>Management Engine (SLAWME)", title_style))
    story.append(HRFlowable(width="80%", thickness=2, color=ACCENT, spaceAfter=20))
    story.append(Paragraph("Comprehensive Technical & Design Evaluation Report", subtitle_style))
    
    story.append(Spacer(1, 60))

    meta_data = [
        [Paragraph("<b>Course Name:</b>", body_style), Paragraph("Programming in Java (Flipped Course)", body_style)],
        [Paragraph("<b>Domain:</b>", body_style), Paragraph("Enterprise Java & Multithreaded Software Engineering", body_style)],
        [Paragraph("<b>Application Type:</b>", body_style), Paragraph("100% Terminal CLI Executable Engine", body_style)],
        [Paragraph("<b>Platform:</b>", body_style), Paragraph("VITyarthi Evaluation Portal", body_style)],
        [Paragraph("<b>Submission Date:</b>", body_style), Paragraph("September 2026", body_style)],
        [Paragraph("<b>Evaluation Status:</b>", body_style), Paragraph("Public GitHub Repository & Verified Executable", body_style)]
    ]
    t_meta = Table(meta_data, colWidths=[150, 300])
    t_meta.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), LIGHT_BG),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('INNERGRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('TOPPADDING', (0,0), (-1,-1), 8),
        ('BOTTOMPADDING', (0,0), (-1,-1), 8),
        ('LEFTPADDING', (0,0), (-1,-1), 12),
    ]))
    story.append(t_meta)
    story.append(PageBreak())

    # ==================== 2. INTRODUCTION ====================
    story.append(Paragraph("1. Introduction", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph(
        "Modern supply chain logistics, distribution centers, and fulfillment hubs depend heavily on precise, high-throughput software systems to manage inventory control, enforce access permissions, and execute order fulfillment. In modern enterprise software development, Java stands as the gold standard due to its cross-platform portabilty, robust object-oriented foundation, rich collections framework, multi-threading support, and expressive Streams API.",
        body_style
    ))
    story.append(Paragraph(
        "The <b>Smart Logistics & Automated Warehouse Management Engine (SLAWME)</b> is an original, standalone command-line application engineered for the <i>Programming in Java</i> flipped course evaluation. It provides an end-to-end operational software platform designed to manage heterogeneous warehouse inventories (such as perishable groceries and electronic hardware), enforce fine-grained Role-Based Access Control (RBAC), process customer orders asynchronously in background threads, and compute real-time stream analytics.",
        body_style
    ))

    # ==================== 3. PROBLEM STATEMENT ====================
    story.append(Paragraph("2. Problem Statement", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph(
        "Contemporary warehouse operations encounter severe operational bottlenecks, including:",
        body_style
    ))
    story.append(Paragraph("• <b>Stock Spoilage & Financial Loss:</b> Perishable items frequently expire unnoticed due to lack of automated date-tracking and temperature threshold monitoring.", body_style))
    story.append(Paragraph("• <b>Unsafe Access & Data Tampering:</b> Shared terminals often lack cryptographically hashed password security or role segregation, allowing unauthorized operators to alter inventory records.", body_style))
    story.append(Paragraph("• <b>Order Dispatch Latency:</b> Synchronous single-threaded processing blocks terminal interaction during long-running fulfillment tasks, limiting throughput.", body_style))
    story.append(Paragraph("• <b>Lack of Real-Time Analytics:</b> Warehouse managers lack instant category-wise valuation metrics required to optimize stocking levels.", body_style))
    story.append(Paragraph("SLAWME resolves these issues by delivering a zero-dependency, 100% terminal CLI system featuring SHA-256 salted security, polymorphic stock models, multithreaded order queues, and Java Stream API intelligence.", body_style))

    # ==================== 4. FUNCTIONAL REQUIREMENTS ====================
    story.append(Paragraph("3. Functional Requirements", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("SLAWME implements three major functional modules meeting all course expectations:", body_style))

    f_reqs = [
        [Paragraph("<b>Module Name</b>", body_style), Paragraph("<b>Key Responsibilities & Functionality</b>", body_style)],
        [Paragraph("<b>1. Security & RBAC Module</b>", body_style), Paragraph("User authentication with SHA-256 salted hashing. Role enforcement across ADMIN, MANAGER, and AUDITOR. Session tracking.", body_style)],
        [Paragraph("<b>2. Inventory Management Module</b>", body_style), Paragraph("Polymorphic CRUD for Perishable and Electronic items. Expiration tracking, storage temp monitoring, and low-stock threshold alerts.", body_style)],
        [Paragraph("<b>3. Order Queue & Analytics Engine</b>", body_style), Paragraph("Asynchronous order placement using multithreaded ExecutorService thread pool (4 workers). Java Stream API category summaries and CSV/JSON persistence.", body_style)]
    ]
    t_freq = Table(f_reqs, colWidths=[140, 310])
    t_freq.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), SECONDARY),
        ('TEXTCOLOR', (0,0), (-1,0), colors.white),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('INNERGRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('TOPPADDING', (0,0), (-1,-1), 6),
        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t_freq)
    story.append(Spacer(1, 10))

    # ==================== 5. NON-FUNCTIONAL REQUIREMENTS ====================
    story.append(Paragraph("4. Non-Functional Requirements", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))

    nfr_data = [
        [Paragraph("<b>Requirement</b>", body_style), Paragraph("<b>Specification & Technical Implementation</b>", body_style)],
        [Paragraph("<b>Performance</b>", body_style), Paragraph("O(1) item lookups using ConcurrentHashMap; non-blocking asynchronous thread pool dispatch.", body_style)],
        [Paragraph("<b>Security</b>", body_style), Paragraph("Cryptographic SHA-256 password hashing with 16-byte random salt per user; strict RBAC checks.", body_style)],
        [Paragraph("<b>Reliability</b>", body_style), Paragraph("ReentrantReadWriteLock data synchronization preventing race conditions during concurrent stock updates.", body_style)],
        [Paragraph("<b>Maintainability</b>", body_style), Paragraph("Clean package separation (com.vityarthi.slawme.*); 100% adherence to SOLID & OOP principles.", body_style)]
    ]
    t_nfr = Table(nfr_data, colWidths=[110, 340])
    t_nfr.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), PRIMARY),
        ('TEXTCOLOR', (0,0), (-1,0), colors.white),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('INNERGRID', (0,0), (-1,-1), 0.5, BORDER_COLOR),
        ('TOPPADDING', (0,0), (-1,-1), 6),
        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t_nfr)

    story.append(PageBreak())

    # ==================== 6. SYSTEM ARCHITECTURE ====================
    story.append(Paragraph("5. System Architecture", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph(
        "SLAWME follows a layered modular architecture. The Presentation Layer handles ANSI-colored terminal interaction, delegating logic to the Service Layer, which operates on the Object Model and relies on thread-safe Storage and Logging utilities.",
        body_style
    ))

    arch_box = [
        [Paragraph("<font color='#FFFFFF'><b>[ PRESENTATION LAYER ]</b> Main CLI Driver & ANSI Terminal Menu</font>", ParagraphStyle('A1', parent=code_style, alignment=TA_CENTER))],
        [Paragraph("<font color='#1A202C'>↓ Invokes Authenticated Actions & Passes DTOs</font>", ParagraphStyle('A2', parent=code_style, alignment=TA_CENTER))],
        [Paragraph("<font color='#FFFFFF'><b>[ SERVICE LAYER ]</b> AuthService | InventoryService | OrderService (ThreadPool)</font>", ParagraphStyle('A3', parent=code_style, alignment=TA_CENTER))],
        [Paragraph("<font color='#1A202C'>↓ Manipulates Domain Entities & Uses Synchronization Locks</font>", ParagraphStyle('A4', parent=code_style, alignment=TA_CENTER))],
        [Paragraph("<font color='#FFFFFF'><b>[ DOMAIN & STORAGE ]</b> AbstractItem / Perishable / Electronic | DataManager | AuditLogger</font>", ParagraphStyle('A5', parent=code_style, alignment=TA_CENTER))]
    ]
    t_arch = Table(arch_box, colWidths=[450])
    t_arch.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (0,0), PRIMARY),
        ('BACKGROUND', (0,2), (0,2), SECONDARY),
        ('BACKGROUND', (0,4), (0,4), ACCENT),
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('TOPPADDING', (0,0), (-1,-1), 6),
        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t_arch)
    story.append(Spacer(1, 15))

    # ==================== 7. DESIGN DIAGRAMS ====================
    story.append(Paragraph("6. Design & UML Diagrams", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))

    story.append(Paragraph("6.1 Class & Component Relationship Diagram", h2_style))
    story.append(Paragraph(
        "• <b>Auditable</b> (Interface) ← Implemented by <b>AbstractItem</b>, <b>User</b>, <b>Order</b><br/>"
        "• <b>WarehouseItem</b> (Interface) ← Implemented by <b>AbstractItem</b><br/>"
        "• <b>AbstractItem</b> (Abstract Class) ← Extended by <b>PerishableItem</b> and <b>ElectronicItem</b><br/>"
        "• <b>InventoryService</b> aggregates `WarehouseItem` instances into a `ConcurrentHashMap`.<br/>"
        "• <b>OrderService</b> holds a thread pool `ExecutorService` and depends on `InventoryService` for stock reduction.",
        body_style
    ))

    story.append(Paragraph("6.2 Order Processing Workflow Sequence", h2_style))
    story.append(Paragraph(
        "1. <b>User</b> enters order via CLI -> <b>Main</b> calls `OrderService.placeOrder()`<br/>"
        "2. <b>OrderService</b> verifies stock via `InventoryService.getItem()`<br/>"
        "3. If stock &lt; requested -> Throws `InsufficientStockException`<br/>"
        "4. Else -> Creates `Order`, marks as `PENDING`, submits task to `ExecutorService`<br/>"
        "5. <b>Background Thread Worker</b> locks item stock, deducts quantity, updates status to `COMPLETED`, logs via `AuditLogger`.",
        body_style
    ))

    story.append(Paragraph("6.3 Entity-Relationship & Persistence Schema", h2_style))
    story.append(Paragraph(
        "• <b>USERS:</b> Username (PK), PasswordHash, Salt, Role<br/>"
        "• <b>INVENTORY (CSV):</b> Type, ID (PK), Name, Category, Price, Qty, ReorderLevel, Param1 (Expiry/Warranty), Param2 (Temp/Voltage)<br/>"
        "• <b>AUDIT_LOGS:</b> Timestamp, Level, User, ActionString",
        body_style
    ))

    # ==================== 8. DESIGN DECISIONS & RATIONALE ====================
    story.append(Paragraph("7. Design Decisions & Rationale", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("• <b>Zero External Runtime Dependencies:</b> Built entirely on Java 22 Standard Library to guarantee seamless CLI execution across any evaluator environment without requiring complex dependency downloads.", body_style))
    story.append(Paragraph("• <b>ReentrantReadWriteLock over Synchronized Methods:</b> Multiple threads can read inventory statistics simultaneously without blocking, while write locks ensure exclusive access during stock updates.", body_style))
    story.append(Paragraph("• <b>Salting with SHA-256:</b> Storing raw passwords or simple hashes exposes credentials to rainbow table attacks. Adding a unique 16-byte random salt per user prevents reverse lookups.", body_style))

    # ==================== 9. IMPLEMENTATION DETAILS ====================
    story.append(Paragraph("8. Implementation Details", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("Key code snippet demonstrating Java 22 Stream API Category Grouping:", h2_style))

    code_snippet = (
        "public Map&lt;String, DoubleSummaryStatistics&gt; getCategoryStatistics() {\n"
        "    rwLock.readLock().lock();\n"
        "    try {\n"
        "        return inventoryMap.values().stream()\n"
        "                .collect(Collectors.groupingBy(\n"
        "                        WarehouseItem::getCategory,\n"
        "                        Collectors.summarizingDouble(WarehouseItem::calculateInventoryValue)\n"
        "                ));\n"
        "    } finally {\n"
        "        rwLock.readLock().unlock();\n"
        "    }\n"
        "}"
    )
    t_code = Table([[Paragraph(code_snippet.replace('\n', '<br/>'), code_style)]], colWidths=[450])
    t_code.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), LIGHT_BG),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('LEFTPADDING', (0,0), (-1,-1), 10),
        ('TOPPADDING', (0,0), (-1,-1), 8),
        ('BOTTOMPADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(t_code)

    story.append(PageBreak())

    # ==================== 10. SCREENSHOTS / RESULTS ====================
    story.append(Paragraph("9. Screenshots & Automated Test Results", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("Terminal Output of Automated Showcase Demo (`build.bat demo`):", h2_style))

    demo_output = (
        "==========================================================================<br/>"
        "SLAWME - Smart Logistics &amp; Warehouse Management Engine Build Script<br/>"
        "==========================================================================<br/>"
        "Using Compiler: javac 22.0.2<br/>"
        "Compiling Java Main Source Files...<br/>"
        "Compiling Unit Tests...<br/>"
        "✓ Compilation Succeeded<br/>"
        "Running Automated CLI Demo Showcase...<br/>"
        "--&gt; Authenticating as Admin... [PASSED]<br/>"
        "--&gt; Added New Electronic Item [E999] Automated Drone... [PASSED]<br/>"
        "--&gt; Executing Asynchronous Customer Order ORD-DEMO-001... [DISPATCHED]<br/>"
        "--&gt; Running Stream API Category Valuation Analytics...<br/>"
        "   Category: Hardware     | Count: 3 | Total Value: $17599.60<br/>"
        "   Category: Robotics     | Count: 1 | Total Value: $10399.92<br/>"
        "✓ Automated Demo Execution Completed Successfully!"
    )
    t_demo = Table([[Paragraph(demo_output, code_style)]], colWidths=[450])
    t_demo.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), DARK_BG),
        ('TEXTCOLOR', (0,0), (-1,-1), colors.white),
        ('BOX', (0,0), (-1,-1), 1, BORDER_COLOR),
        ('LEFTPADDING', (0,0), (-1,-1), 10),
        ('TOPPADDING', (0,0), (-1,-1), 8),
        ('BOTTOMPADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(t_demo)
    story.append(Spacer(1, 10))

    # ==================== 11. TESTING APPROACH ====================
    story.append(Paragraph("10. Testing Approach & Verification", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("The system was validated using unit tests (`TestRunner.java`) covering logic boundaries:", body_style))
    story.append(Paragraph("• <b>AuthServiceTest:</b> Tests valid credentials, incorrect password rejection, and RBAC permission checks.", body_style))
    story.append(Paragraph("• <b>InventoryServiceTest:</b> Validates polymorphic item addition, low-stock threshold queries, Stream API sum valuations, and perishable expiration filtering.", body_style))

    # ==================== 12. CHALLENGES FACED ====================
    story.append(Paragraph("11. Challenges Faced & Solutions", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("• <b>Challenge:</b> Preventing race conditions when background order worker threads deduct stock while managers edit quantities via CLI.<br/>"
                           "<b>Solution:</b> Implemented explicit `ReentrantReadWriteLock` read/write locking in `InventoryService`.", body_style))
    story.append(Paragraph("• <b>Challenge:</b> Ensuring 100% terminal execution without relying on third-party JARs.<br/>"
                           "<b>Solution:</b> Developed custom CSV/JSON parsers and a standalone test runner (`TestRunner.java`) natively executable via Java assertions.", body_style))

    # ==================== 13. LEARNINGS & KEY TAKEAWAYS ====================
    story.append(Paragraph("12. Learnings & Key Takeaways", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("1. Mastering Object-Oriented Polymorphism and Abstract class hierarchies in real-world Java applications.", body_style))
    story.append(Paragraph("2. Harnessing the Java Stream API for clean, readable, and functional data analysis without external SQL databases.", body_style))
    story.append(Paragraph("3. Designing thread-safe multithreaded applications with `ExecutorService` and synchronization locks.", body_style))

    # ==================== 14. FUTURE ENHANCEMENTS ====================
    story.append(Paragraph("13. Future Enhancements", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("• Integration of an embedded SQLite database using JDBC for enterprise scalability.", body_style))
    story.append(Paragraph("• RESTful API service layer using Spring Boot for mobile app connectivity.", body_style))
    story.append(Paragraph("• Barcode and QR-code scanner hardware integration via serial port Java driver.", body_style))

    # ==================== 15. REFERENCES ====================
    story.append(Paragraph("14. References", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=PRIMARY, spaceAfter=10))
    story.append(Paragraph("1. Oracle Java 22 Documentation & API Specifications: https://docs.oracle.com/en/java/", body_style))
    story.append(Paragraph("2. Bloch, Joshua. <i>Effective Java</i> (3rd Edition). Addison-Wesley Professional, 2018.", body_style))
    story.append(Paragraph("3. Goetz, Brian, et al. <i>Java Concurrency in Practice</i>. Addison-Wesley Professional, 2006.", body_style))
    story.append(Paragraph("4. VITyarthi Project Submission Guidelines & Rubric Standard Document.", body_style))

    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"SUCCESS: PDF Report generated successfully: {filename}")

if __name__ == "__main__":
    create_pdf_report()
